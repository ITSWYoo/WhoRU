package com.yoo.ymh.whoru.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-09-18.
 */
public class AppAlarmList {
    @SerializedName("data")
    @Expose
    private List<AppAlarm> data = new ArrayList<AppAlarm>();
    @SerializedName("total")
    @Expose
    private Integer total;

    /**
     *
     * @return
     * The data
     */
    public List<AppAlarm> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<AppAlarm> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }
}
