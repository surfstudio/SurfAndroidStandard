package ru.surfstudio.android.imageloader_sample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
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
                /*.centerCrop()
                .maxWidth(570)
                .maxHeight(9300)*/
                .url("https://s.mdk.zone/i/22096ef7-7f2b-4cd3-920b-a65a032b9e21")
                .error(R.drawable.ic_launcher_background)
                .into(imageView)

        /*Glide.with(this)
                .load("https://s.mdk.zone/i/22096ef7-7f2b-4cd3-920b-a65a032b9e21")
                .into(imageView)*/
    }
}