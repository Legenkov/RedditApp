package com.example.redditapp

data class RedditResponse(val data: RedditData)

data class RedditData(val children: List<RedditChild>, val after: String?)

data class RedditChild(val data: RedditPost)

data class RedditPost(
    val title: String,
    val author: String,
    val thumbnail: String?,
    val url: String,
    val ups: Int,
    val createdUtc: Long
)