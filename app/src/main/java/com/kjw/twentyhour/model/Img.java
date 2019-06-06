
package com.kjw.twentyhour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.nio.Buffer;

public class Img {

    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("contentType")
    @Expose
    private String contentType;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
