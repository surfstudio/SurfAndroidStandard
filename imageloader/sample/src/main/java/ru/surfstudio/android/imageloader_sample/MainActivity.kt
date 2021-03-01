package ru.surfstudio.android.imageloader_sample

import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.gif.GifDrawable
import org.jetbrains.anko.find
import ru.surfstudio.android.imageloader.ImageLoader
import ru.surfstudio.android.logger.Logger

class MainActivity : AppCompatActivity() {

    private val IMAGE_URL = "https://imgs.xkcd.com/comics/regular_expressions.png"
    private val MEME_IMAGE_URL = "https://i.kym-cdn.com/photos/images/newsfeed/001/265/255/f79.png"
    private val VIDEO_URL = "https://static.rendez-vous.ru/files/catalog_videos/472/2513472.mp4"

    private lateinit var imageView: ImageView
    private lateinit var lambdasImageView: ImageView
    private lateinit var transformButton: Button

    private lateinit var svgIv: ImageView

    private lateinit var gifImageView: ImageView
    private lateinit var gifButton: Button

    private lateinit var videoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = find(R.id.imageloader_sample_iv)
        lambdasImageView = find(R.id.imageloader_sample_lambdas_iv)
        transformButton = find(R.id.image_loader_sample_btn)

        var isLoadOriginal = false

        transformButton.setOnClickListener {
            if (isLoadOriginal) loadOriginalImage() else loadTransformedImage()
            isLoadOriginal = !isLoadOriginal
        }

        imageView.post { loadOriginalImage() }
        lambdasImageView.post { loadMemeImage() }

        svgIv = find(R.id.imageloader_sample_svg_iv)
        val svgImageUrl = "https://card2card.zenit.ru/assets/images/banks/yandex.svg"
        loadSvgImage(svgImageUrl)

        gifImageView = find(R.id.imageloader_sample_gif_iv)
        gifButton = find(R.id.image_loader_sample_gif_btn)
        loadGifImage()

        videoImageView = find(R.id.imageloader_video_iv)
        loadVideoPreview()
    }

    private fun loadOriginalImage() {
        ImageLoader
                .with(this)
                .crossFade(500)
                .maxWidth(imageView.width / 2)
                .maxHeight(imageView.height / 2)
                .tile()
                .url(IMAGE_URL, mapOf(Pair("User-Agent", "*")))
                .mask(true, R.drawable.ic_error_state, PorterDuff.Mode.LIGHTEN)
                .signature(Math.random()) // картинка будет грузиться при каждом тапе
                .preview(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }

    private fun loadMemeImage() {
        ImageLoader
                .with(this)
                .url(MEME_IMAGE_URL)
                .into(
                        view = lambdasImageView,
                        onCompleteLambda = { res, imageSource ->
                            Logger.d("onCompleteLambda")
                        },
                        onErrorLambda = {
                            Logger.d("onErrorLambda")
                        }
                )
    }

    private fun loadTransformedImage() {
        ImageLoader
                .with(this)
                .crossFade(500)
                .centerCrop()
                .blur(blurDownSampling = 4)
                .url(IMAGE_URL, mapOf(Pair("User-Agent", "*")))
                .signature(Math.random()) // картинка будет грузиться при каждом тапе
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }

    private fun loadGifImage() {
        ImageLoader
                .with(this)
                .url(R.drawable.android_gif)
                .into(gifImageView, onCompleteLambda = { res, _, _ ->
                    gifImageView.setImageDrawable(res)
                    if (res is GifDrawable) {
                        res.setLoopCount(10)
                        gifButton.setOnClickListener {
                            if (res.isRunning) {
                                res.stop()
                            } else {
                                res.start()
                            }
                        }
                    }
                })
    }

    private fun loadVideoPreview() {
        ImageLoader
                .with(this)
                .url(VIDEO_URL)
                .centerCrop()
                .frame(0L)
                .into(videoImageView)
    }

    private fun loadSvgImage(svgImageUrl: String) {
        ImageLoader
                .with(this)
                .skipCache(true) //для svg недоступен кэш
                .centerCrop()
                .url(svgImageUrl)
                .error(R.drawable.ic_launcher_background)
                .into(svgIv)
    }
}