package com.example.redditapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RedditPostAdapter
    private lateinit var appState: AppState
    private val posts = mutableListOf<RedditPost>()
    private var after: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appState = AppState(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter = RedditPostAdapter(posts)
        recyclerView.adapter = adapter

        val lastPostId = appState.getLastPostId()
        if (lastPostId != null) {
            loadPosts()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadPosts()
                }
            }
        })
    }

    private fun loadPosts() {
        RetrofitClient.instance.getTopPosts(after).enqueue(object : Callback<RedditResponse> {
            override fun onResponse(call: Call<RedditResponse>, response: Response<RedditResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { redditResponse ->
                        posts.addAll(redditResponse.data.children.map { it.data })
                        after = redditResponse.data.after
                        adapter.notifyDataSetChanged() // Уведомляем адаптер об изменении данных
                    }
                }
            }

            override fun onFailure(call: Call<RedditResponse>, t: Throwable) {
            }
        })

    }

    override fun onPause() {
        super.onPause()
        // Сохранение состояния перед выходом
        appState.saveLastPostId("some_post_id") // Сохраните актуальный пост
    }

}