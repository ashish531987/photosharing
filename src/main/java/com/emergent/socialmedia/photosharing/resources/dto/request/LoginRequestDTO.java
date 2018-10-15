package com.emergent.socialmedia.photosharing.resources.dto.request;

import javax.validation.constraints.NotNull;

public class LoginRequestDTO {
    @NotNull
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
