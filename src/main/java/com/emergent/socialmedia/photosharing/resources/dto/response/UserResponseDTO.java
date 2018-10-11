package com.emergent.socialmedia.photosharing.resources.dto.response;

public class UserResponseDTO{
    public UserResponseDTO(Long userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
    }

    private Long userId;
    private String fullName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}