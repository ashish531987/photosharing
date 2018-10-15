package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public interface ThirdPartyAuthorization {
    Optional<User> authorizeToken(String idToken) throws GeneralSecurityException, IOException;
}
