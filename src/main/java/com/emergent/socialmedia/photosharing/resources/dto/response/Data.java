package com.emergent.socialmedia.photosharing.resources.dto.response;

import java.util.ArrayList;
import java.util.List;

public class Data<T> {
    private List<T> children = new ArrayList<>();
    private Long after = -1L;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public Long getAfter() {
        return after;
    }

    public void setAfter(Long after) {
        this.after = after;
    }
}
