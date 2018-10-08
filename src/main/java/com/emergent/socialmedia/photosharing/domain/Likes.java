package com.emergent.socialmedia.photosharing.domain;

import javax.persistence.*;

@Entity
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    private Media media; // Media liked by atleast one user.

    @ManyToOne(cascade=CascadeType.ALL)
    private User user; // Id of the User Who likes this media.

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
}