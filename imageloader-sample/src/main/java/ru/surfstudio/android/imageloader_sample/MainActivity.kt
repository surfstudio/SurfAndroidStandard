package ru.surfstudio.android.imageloader_sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import org.jetbrains.anko.find
import ru.surfstudio.android.imageloader.ImageLoader

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = find(R.id.imageloader_sample_iv)

        ImageLoader
                .with(this)
                //.url("https://pp.userapi.com/c834204/v834204860/3291c/rp3f6C3B6T4.jpg")
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }
}