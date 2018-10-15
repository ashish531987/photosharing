package com.emergent.socialmedia.photosharing.resources;

import com.emergent.socialmedia.photosharing.resources.dto.request.LoginRequestDTO;
import com.emergent.socialmedia.photosharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class UserResource {
    public static final String REST_LOGIN = "/tokensignin";
    @Autowired
    UserService userService;

    @PostMapping(path = REST_LOGIN,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        try {
            return ResponseEntity.ok(userService.login(loginRequestDTO.getIdToken()));
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
        }
    }
}
