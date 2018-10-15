package com.emergent.socialmedia.photosharing.resources.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentsRequestDTO extends AbstractRequestDTO {
    @NotNull
    @Size(min=6, max=30, message = "Comment length should be greater than 6 characters and should be lesser than 30 characters ")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
