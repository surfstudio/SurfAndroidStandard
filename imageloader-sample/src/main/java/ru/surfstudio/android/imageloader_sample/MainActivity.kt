package ru.surfstudio.android.imageloader_sample

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
                .url("https://s.mdk.zone/i/22096ef7-7f2b-4cd3-920b-a65a032b9e21")
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }
}