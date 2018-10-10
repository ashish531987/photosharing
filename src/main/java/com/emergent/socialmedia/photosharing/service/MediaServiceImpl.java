package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.Media;
import com.emergent.socialmedia.photosharing.domain.User;
import com.emergent.socialmedia.photosharing.repositories.MediaRepository;
import com.emergent.socialmedia.photosharing.repositories.UserRepository;
import com.emergent.socialmedia.photosharing.resources.MediaResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public List<Media> getAllMediaOrderByCreatedAtDesc(Integer after, Integer limit) {
        Pageable pageable = PageRequest.of(after, limit);
        return mediaRepository.findAllByOrderByCreatedAtDesc(pageable); // TODO filter own data
    }

    @Override
    public List<Media> getAllMediaOrderByCreatedAtDesc() {
        return mediaRepository.findAllByOrderByCreatedAtDesc(); // TODO filter own data
    }

    @Override
    public Media like(String userId, String mediaId) {
        return null;
    }

    @Override
    public Media dislike(String userId, String mediaId) {
        return null;
    }

    @Override
    public Media comment(String userId, String mediaId, String message) {
        return null;
    }

    @Override
    public Media uncomment(String userId, String mediaId) {
        return null;
    }

    @Override
    public List<Media> getAllMediaLikedByUserId(String userId) {
        return null;
    }

    @Override
    public List<Media> getAllMediaCommentedByUserId(String userId) {
        return null;
    }
}
