package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.Media;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    Media storeMedia(String userId, MultipartFile file);

    Resource getMedia(String mediaId);

    List<Media> getAllMediaOrderByCreatedAtDesc(Integer after, Integer limit);

    List<Media> getAllMediaOrderByCreatedAtDesc();

    Media like(String userId, String mediaId);

    Media dislike(String userId, String mediaId);

    Media comment(String userId, String mediaId, String message);

    Media uncomment(String userId, String mediaId);

    List<Media> getAllMediaLikedByUserId(String userId);

    List<Media> getAllMediaCommentedByUserId(String userId);
}
