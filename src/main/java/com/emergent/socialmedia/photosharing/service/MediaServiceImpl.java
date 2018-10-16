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
import com.emergent.socialmedia.photosharing.resources.dto.response.Data;
import com.emergent.socialmedia.photosharing.resources.dto.response.MediaResponseDTO;
import com.emergent.socialmedia.photosharing.resources.dto.response.ResponseContainerDTO;
import com.emergent.socialmedia.photosharing.service.exceptions.MediaNotFoundException;
import com.emergent.socialmedia.photosharing.service.exceptions.MyFileNotFoundException;
import com.emergent.socialmedia.photosharing.service.exceptions.UserNotFoundException;
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
    public MediaResponseDTO storeMedia(Long userId, MultipartFile file) {
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
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            media.setUser(user);
        } else {
            throw new UserNotFoundException(userId);
        }
        mediaRepository.save(media);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(userId.toString())
                .path(MediaResource.REST_MEDIA)
                .path(String.valueOf(media.getId()))
                .toUriString();
        storageService.store(media.getId(), file);
        media.setDownloadURI(fileDownloadUri);
        mediaRepository.save(media);
        return new MediaResponseDTO().fromMediaAndLikedByME(media, false);
    }

    @Override
    public Resource getMedia(Long mediaId) throws MyFileNotFoundException {
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
        if(optionalMedia.isPresent()){
            Media media = optionalMedia.get();
            return storageService.loadAsResource(String.valueOf(media.getId()));
        } else throw new MediaNotFoundException(mediaId);
    }

    @Override
    public ResponseContainerDTO getAllMediaOrderByCreatedAtDesc(Long userId, Long after, Integer limit) {
        if(after == null) after = 0L;
        ResponseContainerDTO feedResponseDTO = new ResponseContainerDTO();
        List<Media> resultList;

        if(limit != null && after >= 0){
            Date date = new Date();
            Optional<Media> optionalMedia = mediaRepository.findById(after);
            if(optionalMedia.isPresent()) date = optionalMedia.get().getCreatedAt();

            Pageable pageable = PageRequest.of(0, limit, new Sort(Sort.Direction.DESC, Media.MEDIA_ID));
            resultList = mediaRepository.findAllByCreatedAtBefore(date, pageable);
        } else {
            resultList = new ArrayList<>(mediaRepository.findAllByOrderByCreatedAtDesc());
        }
        Data<MediaResponseDTO> data = new Data<>();
        if(!resultList.isEmpty()) {
            for(Media media : resultList) {
                Boolean likedByMe = likedByMe(userId, media.getId());

                data.getChildren().add(new MediaResponseDTO().fromMediaAndLikedByME(media, likedByMe));
            }
            data.setAfter(resultList.get(resultList.size()-1).getId());
        }

        feedResponseDTO.setData(data);
        return feedResponseDTO;
    }

    @Override
    public MediaResponseDTO like(Long userId, Long mediaId) {
        // Step 1 add new like in LikesRepo
        // Step 2 Change count in MediaRepo
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);

        if(optionalMedia.isPresent()){
            if(!likesRepository.findOneByUserIdAndMediaId(userId, mediaId).isPresent()) {
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
                    return new MediaResponseDTO().fromMediaAndLikedByME(media, true);
                } else throw new UserNotFoundException(userId);

            } else return new MediaResponseDTO().fromMediaAndLikedByME(
                    optionalMedia.get(),
                    true);

        }else{
            throw new MediaNotFoundException(mediaId);
        }
    }

    @Override
    public MediaResponseDTO dislike(Long userId, Long mediaId) {
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
        Optional<Likes> optionalLikes = likesRepository.findOneByUserIdAndMediaId(userId, mediaId);
        if(optionalMedia.isPresent()){
            if(optionalLikes.isPresent()) {
                Media media = optionalMedia.get();
                // Fetch User
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    likesRepository.delete(optionalLikes.get());
                    media.setLikesCount(media.getLikesCount() - 1);
                    media = mediaRepository.save(media);
                    return new MediaResponseDTO().fromMediaAndLikedByME(media, false);
                } else throw new UserNotFoundException(userId);
            } else{
                return new MediaResponseDTO().fromMediaAndLikedByME(
                        optionalMedia.get(),
                        false);
            }
        } else throw new MediaNotFoundException(mediaId);
    }

    @Override
    public MediaResponseDTO comment(Long userId, Long mediaId, String message) {
        // Step 1 add new comment in CommentsRepo
        // Step 2 Change count in MediaRepo
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);

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
                mediaRepository.save(media);
                return new MediaResponseDTO().fromMediaAndLikedByME(media, likedByMe(userId, media.getId()));
            } else throw new UserNotFoundException(userId);

        } else throw new MediaNotFoundException(mediaId);
    }

    @Override
    public MediaResponseDTO uncomment(Long userId, Long mediaId, Long commentId) {
        Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
        if (optionalMedia.isPresent()) {
            Media media = optionalMedia.get();
            // Fetch User
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                commentsRepository.deleteById(commentId);
                media.setCommentsCount(media.getCommentsCount() - 1);
                mediaRepository.save(media);
                return new MediaResponseDTO().fromMediaAndLikedByME(media, likedByMe(userId, mediaId));
            } else throw new UserNotFoundException(userId);

        } else throw new MediaNotFoundException(mediaId);
    }

    @Override
    public List<Media> getAllMediaLikedByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Media> getAllMediaCommentedByUserId(Long userId) {
        return null;
    }

    @Override
    public ResponseContainerDTO getAllCommentsOrderByCreatedAtDesc(Long userId, Long mediaId, Long after, Integer limit) {
        if(after == null) after = 0L;
        ResponseContainerDTO commentsResponseDTO = new ResponseContainerDTO();
        List<Comments> resultList;

        if(limit != null && after >= 0){
            Date date = new Date();
            Optional<Comments> optionalComment = commentsRepository.findById(after);
            if(optionalComment.isPresent()) date = optionalComment.get().getCommentedAt();
            Pageable pageable = PageRequest.of(0, limit, new Sort(Sort.Direction.DESC, Comments.COMMENTS_ID));
            resultList = commentsRepository.findAllByMediaIdAndCommentedAtBefore(mediaId, date, pageable);
        } else {
            Pageable pageable = PageRequest.of(0, 100, new Sort(Sort.Direction.DESC, Comments.COMMENTED_AT));
            resultList = new ArrayList<>(commentsRepository.findAllByMediaId(mediaId, pageable));
        }
        Data<Comments> data = new Data<>();
        if(!resultList.isEmpty()) {
            data.setChildren(resultList);
            data.setAfter(resultList.get(resultList.size()-1).getId());
        }

        commentsResponseDTO.setData(data);
        return commentsResponseDTO;
    }
    private Boolean likedByMe(Long userId, Long mediaId) {
        // Assumption - Traversing over all media liked by user media vs. all users liking this media
        Optional<List<Likes>> optionalLikesList= likesRepository.findAllByUserId(userId);

        Boolean likedByMe = false;
        if(optionalLikesList.isPresent()){
            likedByMe = optionalLikesList.get().parallelStream().anyMatch((l)-> l.getMedia().getId().equals(mediaId));
        }
        return likedByMe;
    }

}
