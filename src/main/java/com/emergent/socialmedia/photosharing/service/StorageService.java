package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.service.exceptions.MyFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void store(Long mediaId, MultipartFile file);
    Resource loadAsResource(String mediaId) throws MyFileNotFoundException;
}
