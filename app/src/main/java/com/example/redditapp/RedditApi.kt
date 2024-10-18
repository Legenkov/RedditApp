package com.example.redditapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RedditApi {
    // Ставлю лимит 10 постов
    @GET("/r/all/top.json?limit=10")
    suspend fun getTopPosts(): RedditResponse
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RedditApi by lazy {
        retrofit.create(RedditApi::class.java)
    }
}