package com.yoo.ymh.whoru.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Yoo on 2016-09-17.
 */
public class FcmToken {

    @SerializedName("_token")
    @Expose
    private String token;

    /**
     *
     * @return
     * The token
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     * The _token
     */
    public void setToken(String token) {
        this.token = token;
    }

}
