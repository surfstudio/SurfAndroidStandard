package ru.surfstudio.android.imageloader_sample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find
import ru.surfstudio.android.imageloader.ImageLoader
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.utilktx.ktx.convert.toBitmap

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
                .into({ drawable, _ ->
                    val bmp = drawable.toBitmap()
                    val newBm = Bitmap.createScaledBitmap(bmp, (bmp.width * 0.5).toInt(), (bmp.height * 0.2).toInt(), true)
                    imageView.setImageBitmap(newBm)
                }, {
                    imageView.setImageDrawable(it)
                })
    }
}