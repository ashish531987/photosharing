package com.emergent.socialmedia.photosharing.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column( unique = true, nullable = false)
    private String email;

    private String fullName;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Media> uploadedMedia = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Likes> likedMedia = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Comments> commentedMedia = new HashSet<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Media> getUploadedMedia() {
        return uploadedMedia;
    }

    public void setUploadedMedia(Set<Media> uploadedMedia) {
        this.uploadedMedia = uploadedMedia;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Likes> getLikedMedia() {
        return likedMedia;
    }

    public void setLikedMedia(Set<Likes> likedMedia) {
        this.likedMedia = likedMedia;
    }

    public Set<Comments> getCommentedMedia() {
        return commentedMedia;
    }

    public void setCommentedMedia(Set<Comments> commentedMedia) {
        this.commentedMedia = commentedMedia;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(id).append(fullName).append(email).toString();
    }
}
