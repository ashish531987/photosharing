package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface UserService {
    User login(String idToken) throws GeneralSecurityException, IOException;
}
