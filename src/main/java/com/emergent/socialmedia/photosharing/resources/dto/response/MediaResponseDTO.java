package com.emergent.socialmedia.photosharing.resources.dto.response;

import com.emergent.socialmedia.photosharing.domain.Media;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaResponseDTO {
    public MediaResponseDTO() {
    }

    public MediaResponseDTO(Long id, String fileName, Long fileSize, UserResponseDTO userResponseDTO, String downloadURI, Boolean likedByMe, long commentsCount, long likesCount) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.userResponseDTO = userResponseDTO;
        this.downloadURI = downloadURI;
        this.likedByMe = likedByMe;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
    }

    private Long id;

    private String fileName;

    private Long fileSize;

    private UserResponseDTO userResponseDTO;

    private String downloadURI;

    private Boolean likedByMe = false;

    private long commentsCount;

    private long likesCount;

    public Boolean getLikedByMe() {
        return likedByMe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @JsonProperty("user")
    public UserResponseDTO getUserResponseDTO() {
        return userResponseDTO;
    }

    public void setUserResponseDTO(UserResponseDTO userResponseDTO) {
        this.userResponseDTO = userResponseDTO;
    }

    public String getDownloadURI() {
        return downloadURI;
    }

    public void setDownloadURI(String downloadURI) {
        this.downloadURI = downloadURI;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public void setLikedByMe(Boolean likedByMe) {
        this.likedByMe = likedByMe;
    }

    public MediaResponseDTO fromMediaAndLikedByME(Media media, Boolean likedByMe){
        return new MediaResponseDTO(
                media.getId(),
                media.getFileName(),
                media.getFileSize(),
                new UserResponseDTO(media.getUser().getId(), media.getUser().getFullName()),
                media.getDownloadURI(),
                likedByMe,
                media.getCommentsCount(),
                media.getLikesCount()
        );
    }
}
