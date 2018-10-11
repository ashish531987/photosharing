package com.emergent.socialmedia.photosharing.resources.dto.response;

public abstract class AbstractResponseDTO {
    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Meta meta;
    private Data data;
}
