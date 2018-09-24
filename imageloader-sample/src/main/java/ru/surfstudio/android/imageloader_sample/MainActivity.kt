package ru.surfstudio.android.imageloader_sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import org.jetbrains.anko.find
import ru.surfstudio.android.imageloader.ImageLoader
import ru.surfstudio.android.imageloader.data.CacheStrategy

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = find(R.id.imageloader_sample_iv)

        ImageLoader
                .with(this)
                .centerCrop()
                .cacheStrategy(CacheStrategy.CACHE_TRANSFORMED)
                .maxWidth(570)
                .maxHeight(9300)
                .crossFade(1000)
                .roundedCorners(true, 20)
                .url("http://i1.sndcdn.com/avatars-000212747765-zn2480-original.jpg")
                //.url(R.drawable.a123321)
                .preview(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }
}