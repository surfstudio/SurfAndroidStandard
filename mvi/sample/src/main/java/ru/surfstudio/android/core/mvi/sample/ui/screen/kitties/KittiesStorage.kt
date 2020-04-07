package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.Kitten
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import javax.inject.Inject
import kotlin.random.Random

@PerScreen
internal class KittiesStorage @Inject constructor() {

    private var kittiesCount = 0
    private var kitties = emptyList<Kitten>()
    private var meowsCount = 0

    init {
        generateNewKittiesList()
    }

    fun getKittenOfTheWeek(): Kitten = kitties.random()

    fun getMeowsCount(): Int = meowsCount

    fun getKittiesCount(): Int {
        kittiesCount = when {
            kittiesCount == 0 -> kitties.size
            else -> {
                val randomNumber = Random(kittiesCount).nextInt(0, 16)
                kittiesCount + randomNumber
            }
        }
        return kittiesCount
    }

    fun getPopularKitties(): List<Kitten> = kitties.take(10)

    fun getPaginatedKitties(offset: Int, limit: Int = 20): DataList<Kitten> {
        val nextOffset = (offset + limit)
                .let { if (it > kitties.size) kitties.size else it }

        val kittiesSlice = kitties.subList(offset, nextOffset)
        return DataList(kittiesSlice, limit, nextOffset, kitties.size)
    }

    fun sendMeow() {
        meowsCount++
    }

    fun generateNewKittiesList() {
        kitties = listOf(
                Kitten("McLaren", "https://pbs.twimg.com/profile_images/3380392919/76975e09364cb957e5fcd6ce4dd42291.jpeg"),
                Kitten("Tofi-fe-fe", "https://static-s.aa-cdn.net/img/ios/1368361514/78ff32576592cdb95e42c7884f844811?v=1"),
                Kitten("BigMood", "https://pbs.twimg.com/profile_images/3543311953/0602004fc7b8d532cfe58d1de2eb1e1a.png"),
                Kitten("MeowKnight", "https://static-s.aa-cdn.net/img/ios/1171095810/058be2961cda1d758bda6e0e7755b84c"),
                Kitten("SimonsCats", "https://static-s.aa-cdn.net/img/ios/1286736767/abb01fe5abdb4d458255bd5b2c20e6bd?v=1"),
                Kitten("TedTheKilla", "https://pbs.twimg.com/profile_images/713844613803614209/164J377Y_400x400.jpg"),
                Kitten("yeah, koala", "https://miro.medium.com/fit/c/256/256/0*nbw7JHwcdV9LXOy_"),
                Kitten("Whitey", "https://a.wattpad.com/useravatar/UglyKitten.256.124755.jpg"),
                Kitten("SillyOnE", "https://kittencantos.files.wordpress.com/2010/12/kittengravatar2.jpg"),
                Kitten("woaw, kit", "https://pbs.twimg.com/profile_images/694718778911764480/3dH7jGTE_400x400.jpg"),
                Kitten("1face2pack", "https://static-s.aa-cdn.net/img/ios/1263195597/405528a51133a5984ea68fc01d552fd8?v=1"),
                Kitten("Mimimiorgy", "https://placekitten.com/256/256?image=2"),
                Kitten("nekoGuy", "https://i.pinimg.com/originals/23/c6/3e/23c63e21a8e3ee948113d2607593eddb.jpg"),
                Kitten("wt-im-dng-hr", "https://apprecs.org/ios/images/app-icons/256/77/878625143.jpg")
        ).let { kitties ->
            val populatedKitties = mutableListOf<Kitten>()
            repeat(10) { index ->
                val mappedKitties = kitties.map { it.copy(name = "${it.name}#$index") }
                populatedKitties.addAll(mappedKitties)
            }
            populatedKitties
        }.shuffled()
    }
}