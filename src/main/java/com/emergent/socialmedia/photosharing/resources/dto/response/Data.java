package com.emergent.socialmedia.photosharing.resources.dto.response;

import com.emergent.socialmedia.photosharing.domain.Media;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Media> children = new ArrayList<>();
    private Long after = -1L;

    public List<Media> getChildren() {
        return children;
    }

    public void setChildren(List<Media> children) {
        this.children = children;
    }

    public Long getAfter() {
        return after;
    }

    public void setAfter(Long after) {
        this.after = after;
    }
}
