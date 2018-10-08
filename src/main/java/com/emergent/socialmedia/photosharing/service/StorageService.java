package com.emergent.socialmedia.photosharing.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void store(Long mediaId, MultipartFile file);
    Resource loadAsResource(String mediaId);
}
