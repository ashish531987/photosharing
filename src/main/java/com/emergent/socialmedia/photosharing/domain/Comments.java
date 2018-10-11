package com.emergent.socialmedia.photosharing.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comments {
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Media media; // Media liked by atleast one user.

    @JsonIgnore
    @ManyToOne
    private User user; // Id of the User Who likes this media.

    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date commentedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Date commentedAt) {
        this.commentedAt = commentedAt;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
