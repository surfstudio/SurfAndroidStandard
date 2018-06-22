package ru.surfstudio.android.imageloader_sample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find
import ru.surfstudio.android.imageloader.ImageLoader
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.utilktx.ktx.convert.toBitmap
import ru.surfstudio.android.utilktx.util.ViewUtil

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = find(R.id.imageloader_sample_iv)

        ImageLoader
                .with(this)
                .url("https://s.mdk.zone/i/8ba630e3-a996-4b44-83b5-2a1c346cc3e7")
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }
}