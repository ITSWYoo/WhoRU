package com.yoo.ymh.whoru.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Yoo on 2016-09-15.
 */
public class AppUser {

    @SerializedName("_code")
    @Expose
    private Integer code;
    @SerializedName("_sessionId")
    @Expose
    private String sessionId;

    /**
     * @return The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code The _code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return The sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId The _sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
