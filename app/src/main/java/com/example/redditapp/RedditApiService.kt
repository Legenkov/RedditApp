package com.example.redditapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApiService {
    @GET("r/all/top.json?limit=10")
    fun getTopPosts(@Query("after") after: String? = null): Call<RedditResponse>
}