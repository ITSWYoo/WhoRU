package com.yoo.ymh.whoru.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-09-10.
 */
public class RemovedAppContactList {
        @SerializedName("data")
        @Expose
        private List<Integer> data = new ArrayList<>();

        /**
         *
         * @return
         * The data
         */
        public List<Integer> getData() {
            return data;
        }

        /**
         *
         * @param data
         * The data
         */
        public void setData(List<Integer> data) {
            this.data = data;
        }


}
