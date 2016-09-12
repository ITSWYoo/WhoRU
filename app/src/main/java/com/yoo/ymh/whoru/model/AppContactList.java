package com.yoo.ymh.whoru.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-09-02.
 */
public class AppContactList {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("data")
    @Expose
    private List<AppContact> data = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<AppContact> getData() {
        return data;
    }

    public void setData(List<AppContact> data) {
        this.data = data;
    }
}
