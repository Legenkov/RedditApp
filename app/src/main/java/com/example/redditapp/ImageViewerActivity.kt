package com.example.redditapp

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ImageViewerActivity : AppCompatActivity() {
    private val REQUEST_CODE = 100

    private lateinit var appState: AppState
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        var imageUrl = intent.getStringExtra("IMAGE_URL")
        val imageView: ImageView = findViewById(R.id.imageView)
        val saveButton: Button = findViewById(R.id.save_button)

        // Загрузка изображения с помощью Glide
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // Запрос разрешения на запись в хранилище
        requestStoragePermission()

        // Обработка клика по кнопке сохранения
        saveButton.setOnClickListener {
            saveImageToGallery(imageUrl)
        }

        // Закрытие активности по клику на изображение
        imageView.setOnClickListener {
            finish()
        }
        appState = AppState(this)

        // Проверяем SharedPreferences на наличие сохраненного URL
        imageUrl = appState.getImageUrl()

        // Если URL найден, загружаем изображение
        if (imageUrl == null) {
            imageUrl = intent.getStringExtra("IMAGE_URL")
        }

        imageUrl?.let {
            Glide.with(this)
                .load(it)
                .into(findViewById(R.id.imageView))
        }

        // Закрытие Activity при нажатии на изображение
        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            finish() // Закрываем Activity
        }
    }

    override fun onPause() {
        super.onPause()
        // Сохраняем состояние перед выходом
        appState.saveImageUrl(imageUrl)
    }


    private fun saveImageToGallery(imageUrl: String?) {
        if (imageUrl != null) {
            // Загрузка изображения из URL
            Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        // Сохранение изображения в галерею
                        saveBitmapToGallery(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                Toast.makeText(this, "Image saved to gallery!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}