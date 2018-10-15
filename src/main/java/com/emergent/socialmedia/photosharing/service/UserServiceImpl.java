package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ThirdPartyAuthorization authorization;

    @Override
    public User login(String idTokenString) throws GeneralSecurityException, IOException {
        Optional<User> optionalUser = authorization.authorizeToken(idTokenString);
        return optionalUser.orElse(null);
    }
}
