package com.yoo.ymh.whoru.http;

import com.yoo.ymh.whoru.model.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Yoo on 2016-08-09.
 */
public interface MainRecyclerviewRetrofitService {
    public static final String URL = "https://api.github.com/";

    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user")String user);
}
