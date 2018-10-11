package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.Media;
import com.emergent.socialmedia.photosharing.resources.dto.response.AbstractResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    Media storeMedia(String userId, MultipartFile file);

    Resource getMedia(String mediaId);

    AbstractResponseDTO getAllMediaOrderByCreatedAtDesc(Long after, Integer limit);

    Media like(Long userId, Long mediaId);

    Media dislike(Long userId, Long mediaId);

    Media comment(Long userId, Long mediaId, String message);

    Media uncomment(Long userId, Long mediaId);

    List<Media> getAllMediaLikedByUserId(Long userId);

    List<Media> getAllMediaCommentedByUserId(Long userId);
}
