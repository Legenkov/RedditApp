package com.example.redditapp

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class RedditPostAdapter(private val posts: List<RedditPost>) :
    RecyclerView.Adapter<RedditPostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.post_title)
        private val author: TextView = itemView.findViewById(R.id.post_author)
        private val thumbnail: ImageView = itemView.findViewById(R.id.post_thumbnail)
        private val ups: TextView = itemView.findViewById(R.id.post_ups)
        private val timeTextView: TextView = itemView.findViewById(R.id.postTime)


        fun bind(post: RedditPost) {
            title.text = post.title
            author.text = "Posted by: ${post.author}"
            ups.text = "${post.ups} upvotes"
            timeTextView.text = convertTime(post.createdUtc)



            // Загрузка изображения с помощью Glide
            Glide.with(itemView)
                .load(post.thumbnail ?: "")
                .into(thumbnail)
            // Обработка клика по миниатюре
            thumbnail.setOnClickListener {
                // Проверяем, что thumbnail не пустое значение
                if (!post.thumbnail.isNullOrEmpty()) {
                    val intent = Intent(itemView.context, ImageViewerActivity::class.java).apply {
                        putExtra("IMAGE_URL", post.thumbnail) // Передаем URL изображения
                    }
                    itemView.context.startActivity(intent)
                }
            }


        }

        private fun convertTime(timestamp: Long): String {
            val currentTime = System.currentTimeMillis() / 1000 // Текущее время в секундах
            val difference = currentTime - timestamp // Разница во времени в секундах

            Log.d("TimeConversion", "timestamp: $timestamp, currentTime: $currentTime, difference: $difference")

            val minutes = TimeUnit.SECONDS.toMinutes(difference)
            val hours = TimeUnit.SECONDS.toHours(difference)
            val days = TimeUnit.SECONDS.toDays(difference)

            return when {
                days > 0 -> "$days days ago"
                hours > 0 -> "$hours hours ago"
                minutes > 0 -> "$minutes minutes ago"
                else -> "just now"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount() = posts.size
}