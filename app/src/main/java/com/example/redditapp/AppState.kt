package com.example.redditapp

import android.content.Context

class AppState(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveImageUrl(url: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("KEY_IMAGE_URL", url)
        editor.apply()
    }

    fun getImageUrl(): String? {
        return sharedPreferences.getString("KEY_IMAGE_URL", null)
    }

    fun saveLastPostId(postId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("KEY_LAST_POST_ID", postId)
        editor.apply()
    }

    fun getLastPostId(): String? {
        return sharedPreferences.getString("KEY_LAST_POST_ID", null)
    }

}