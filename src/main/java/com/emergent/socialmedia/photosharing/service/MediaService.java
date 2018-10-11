package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.Media;
import com.emergent.socialmedia.photosharing.resources.dto.response.AbstractResponseDTO;
import com.emergent.socialmedia.photosharing.resources.dto.response.MediaResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    MediaResponseDTO storeMedia(String userId, MultipartFile file);

    Resource getMedia(String mediaId);

    AbstractResponseDTO getAllMediaOrderByCreatedAtDesc(Long userId, Long after, Integer limit);

    MediaResponseDTO like(Long userId, Long mediaId);

    MediaResponseDTO dislike(Long userId, Long mediaId);

    MediaResponseDTO comment(Long userId, Long mediaId, String message);

    MediaResponseDTO uncomment(Long userId, Long mediaId, Long commentId);

    List<Media> getAllMediaLikedByUserId(Long userId);

    List<Media> getAllMediaCommentedByUserId(Long userId);

    AbstractResponseDTO getAllCommentsOrderByCreatedAtDesc(Long userId, Long mediaId, Long after, Integer limit);
}
