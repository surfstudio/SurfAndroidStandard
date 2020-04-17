package ru.surfstudio.android.navigation.sample.app.screen.main.cart.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.sample.R

class ImageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false) as TextView
        val route = ImageRoute(arguments)
        view.text = "Image\nFragment\n${route.index}"
        return view
    }
}