package ru.surfstudio.android.custom_view_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_shadow.*
import ru.surfstudio.android.easyadapter.EasyAdapter

class ShadowLayoutActivity: AppCompatActivity() {

    private val easyAdapter = EasyAdapter()
    private val shadowItemController = ShadowItemController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow)

        shadow_items_rv.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = easyAdapter
        }

        easyAdapter.setData(SAMPLE_URLS, shadowItemController)
    }

    private companion object {
        val SAMPLE_URLS = listOf(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/James_Hetfielt.jpg/220px-James_Hetfielt.jpg",
                "https://i.pinimg.com/originals/1c/d5/d1/1cd5d1489e0a46604df5bde88dd863bf.jpg",
                "https://7lafa.com/musicians/audioslave.jpg",
                "https://s15.stc.all.kpcdn.net/share/i/12/5986078/inx960x640.jpg",
                "https://lh3.googleusercontent.com/proxy/40OMVGQ7O_JkCqXezCrN5ONYThhCzHB4WzAMNnOukWmvEOBFtNdpgMOzFA265EG1vOfhKibqqD8QH02lZgzDuCiYN_bZbT89phjn5cePO1aU1mbrdOWyYA",
                "https://www.kultoboz.ru/sites/default/files/images/reviews/music/ac-dc.jpg",
                "https://img.audiomania.ru/images/content/Queen_geektimes_3_2016.jpeg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSNPalFXx50QtsHbwkQ_RuK7id21KEkoyUwoI1U2XDkf4xOug-3&usqp=CAU"
        )
    }
}