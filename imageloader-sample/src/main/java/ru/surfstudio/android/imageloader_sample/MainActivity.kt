package ru.surfstudio.android.imageloader_sample

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.find
import ru.surfstudio.android.imageloader.ImageLoader
import ru.surfstudio.android.imageloader.util.BlurStrategy

class MainActivity : AppCompatActivity() {

    private val IMAGE_URL = "https://cdn-images-1.medium.com/max/2000/1*dT8VX9g8ig6lxmobTRmCiA.jpeg"

    private lateinit var imageView: ImageView
    private lateinit var transformButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = find(R.id.imageloader_sample_iv)
        val svgImageUrl = "https://card2card.zenit.ru/assets/images/banks/yandex.svg"
        val imageUrl = "https://s.mdk.zone/i/22096ef7-7f2b-4cd3-920b-a65a032b9e21"
        transformButton = find(R.id.image_loader_sample_btn)

        var isLoadOriginal = false

        transformButton.setOnClickListener {
            if (isLoadOriginal) loadOriginalImage() else loadTransformedImage()
            isLoadOriginal = !isLoadOriginal
        }

        loadOriginalImage()

    }

    private fun loadOriginalImage() {
        ImageLoader
                .with(this)
                .skipCache(true)
                .crossFade(500)
                .centerCrop()
                .maxWidth(570)
                .maxHeight(9300)
                .crossFade(1000)
                .url(svgImageUrl)
                .url(IMAGE_URL)
                .force()
                .preview(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }

    private fun loadTransformedImage() {
        ImageLoader
                .with(this)
                .crossFade(500)
                .centerCrop()
                .blur(blurDownSampling = 4)
                .url(IMAGE_URL)
                .force()
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }
}