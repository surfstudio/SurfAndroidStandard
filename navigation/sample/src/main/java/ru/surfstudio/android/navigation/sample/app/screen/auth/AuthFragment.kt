package ru.surfstudio.android.navigation.sample.app.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.command.fragment.ReplaceHard
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.main.MainRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.gallery.GalleryRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.gallery.image.ImageRoute

class AuthFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_auth, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mainRoute = MainRoute()
        val galleryRoute = GalleryRoute.Tab()
        auth_btn.setOnClickListener {
            App.executor.execute(
                    listOf(
                            ReplaceHard(mainRoute), //opens main screen
                            Replace(galleryRoute, sourceTag = mainRoute.getId()), //opens gallery tab in main
                            Replace(ImageRoute(1), sourceTag = galleryRoute.getId()), //opens first nested image in gallery
                            Replace(ImageRoute(2), sourceTag = galleryRoute.getId()) //opens second nested image in gallery
                    )
            )
        }
    }
}