package ru.surfstudio.android.navigation.sample.app.screen.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_gallery.*
import ru.surfstudio.android.navigation.command.fragment.RemoveLast
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.main.cart.image.ImageRoute
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations

class GalleryTabFragment : Fragment(), FragmentNavigationContainer {

    override val containerId: Int = R.id.gallery_fragment_container

    var currentPicture = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null)
            addPicture()

        gallery_next_image_btn.setOnClickListener { addPicture() }
        gallery_prev_image_btn.setOnClickListener { removePicture() }
    }

    private fun addPicture() {
        currentPicture++
        App.navigator.execute(Replace(ImageRoute(currentPicture), sourceTag = tag!!))
    }

    private fun removePicture() {
        currentPicture--
        App.navigator.execute(RemoveLast(sourceTag = tag!!))
    }
}