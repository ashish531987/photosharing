package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.Comments;
import com.emergent.socialmedia.photosharing.domain.Likes;
import com.emergent.socialmedia.photosharing.domain.Media;
import com.emergent.socialmedia.photosharing.domain.User;
import com.emergent.socialmedia.photosharing.repositories.CommentsRepository;
import com.emergent.socialmedia.photosharing.repositories.LikesRepository;
import com.emergent.socialmedia.photosharing.repositories.MediaRepository;
import com.emergent.socialmedia.photosharing.repositories.UserRepository;
import com.emergent.socialmedia.photosharing.resources.MediaResource;
import com.emergent.socialmedia.photosharing.resources.dto.response.AbstractResponseDTO;
import com.emergent.socialmedia.photosharing.resources.dto.response.Data;
import com.emergent.socialmedia.photosharing.resources.dto.response.FeedResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    private StorageService storageService;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Override
    public Media storeMedia(String userId, MultipartFile file) {
        /*
            1. Create New Media and populate known properties.
            2. Fetch User.
            3. Store Media record with User and new file details.
            4. Store new file.222
            5. Update Media with downloadURI.
         */
        Media media = new Media();
        media.setCreatedAt(new Date());
        media.setFileName(file.getOriginalFilename());
        media.setFileSize(file.getSize());

        // Fetch User
        Optional<User> optionalUser = userRepository.findById(Integer.valueOf(userId).longValue());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            media.setUser(user);
        } // TODO throw exception
        mediaRepository.save(media);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(userId)
                .path(MediaResource.REST_MEDIA)
                .path(String.valueOf(media.getId()))
                .toUriString();
        storageService.store(media.getId(), file);
        media.setDownloadURI(fileDownloadUri);
        mediaRepository.save(media);
        return media;
    }

    @Override
    public Resource getMedia(String mediaId) {
        Optional<Media> optionalMedia = mediaRepository.findById(Long.valueOf(mediaId));
        if(optionalMedia.isPresent()){
            Media media = optionalMedia.get();
            return storageService.loadAsResource(String.valueOf(media.getId()));
        } else return null; // TODO throw exception
    }

    @Override
    public AbstractResponseDTO getAllMediaOrderByCreatedAtDesc(Long after, Integer limit) {
        if(after == null) after = 0L;

        AbstractResponseDTO feedResponseDTO = new FeedResponseDTO();
        List<Media> resultList;

        if(limit != null && after >= 0){
            Date date = new Date();
            Optional<Media> optionalMedia = mediaRepository.findById(after);
            if(optionalMedia.isPresent()) date = optionalMedia.get().getCreatedAt();

            Pageable pageable = PageRequest.of(0, limit, new Sort(Sort.Direction.DESC, "id"));
            resultList = mediaRepository.findAllByCreatedAtBefore(date, pageable); // TODO filter own data
        } else {
            resultList = new ArrayList<>(mediaRepository.findAllByOrderByCreatedAtDesc());
        }
        Data data = new Data();
        if(!resultList.isEmpty()) {
            data.setChildren(resultList);
            data.setAfter(resultList.get(resultList.size()-1).getId());
        }

        feedResponseDTO.setData(data);
        return feedResponseDTO;
    }

    @Override
    public Media like(Long userId, Long mediaId) {
        // Step 1 add new like in LikesRepo
        // Step 2 Change count in MediaRepo
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);

        if(!likesRepository.findOneByUserIdAndMediaId(userId, mediaId).isPresent()) {
            if (optionalMedia.isPresent()) {
                Media media = optionalMedia.get();
                // Fetch User
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    Likes like = new Likes();
                    like.setMedia(media);
                    like.setUser(user);
                    likesRepository.save(like);
                    media.setLikesCount(media.getLikesCount() + 1);
                    media = mediaRepository.save(media);
                    return media;
                }

            } // TODO Throw exception
        } else{
            return optionalMedia.orElse(null); // TODO Throw exception
        }
        return null;
    }

    @Override
    public Media dislike(Long userId, Long mediaId) {
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
        Optional<Likes> optionalLikes = likesRepository.findOneByUserIdAndMediaId(userId, mediaId);
        if(optionalLikes.isPresent()) {
            if (optionalMedia.isPresent()) {
                Media media = optionalMedia.get();
                // Fetch User
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    likesRepository.delete(optionalLikes.get());
                    media.setLikesCount(media.getLikesCount() - 1);
                    media = mediaRepository.save(media);
                    return media;
                }

            } // TODO Throw exception
        } else{
            return optionalMedia.orElse(null); // TODO Throw exception
        }
        return null;
    }

    @Override
    public Media comment(Long userId, Long mediaId, String message) {
        // Step 1 add new comment in CommentsRepo
        // Step 2 Change count in MediaRepo
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);

        if(!commentsRepository.findOneByUserIdAndMediaId(userId, mediaId).isPresent()) {
            if (optionalMedia.isPresent()) {
                Media media = optionalMedia.get();
                // Fetch User
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    Comments comments = new Comments();
                    comments.setMedia(media);
                    comments.setUser(user);
                    comments.setComment(message);
                    comments.setCommentedAt(new Date());
                    commentsRepository.save(comments);
                    media.setCommentsCount(media.getCommentsCount() + 1);
                    media = mediaRepository.save(media);
                    return media;
                }

            } // TODO Throw exception
        } else{
            return optionalMedia.orElse(null); // TODO Throw exception
        }
        return null;
    }

    @Override
    public Media uncomment(Long userId, Long mediaId) {
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
        Optional<Comments> optionalComments = commentsRepository.findOneByUserIdAndMediaId(userId, mediaId);
        if(optionalComments.isPresent()) {
            if (optionalMedia.isPresent()) {
                Media media = optionalMedia.get();
                // Fetch User
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    commentsRepository.delete(optionalComments.get());
                    media.setCommentsCount(media.getCommentsCount() - 1);
                    media = mediaRepository.save(media);
                    return media;
                }

            } // TODO Throw exception
        } else{
            return optionalMedia.orElse(null); // TODO Throw exception
        }
        return null;
    }

    @Override
    public List<Media> getAllMediaLikedByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Media> getAllMediaCommentedByUserId(Long userId) {
        return null;
    }
}
