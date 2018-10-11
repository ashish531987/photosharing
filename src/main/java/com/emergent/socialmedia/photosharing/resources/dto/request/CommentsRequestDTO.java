package com.emergent.socialmedia.photosharing.resources.dto.request;

public class CommentsRequestDTO extends AbstractRequestDTO {
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
