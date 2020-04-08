package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_kitties.*
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesEvent.Input
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.controller.DividerItemController
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.controller.HeaderItemController
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.controller.KittenItemController
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.DividerType
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.DividerUi
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.message.MessageController
import javax.inject.Inject

internal class KittiesActivityView :
        BaseReactActivityView(),
        SingleHubOwner<KittiesEvent> {

    private val easyAdapter = EasyAdapter()
    private val headerItemController = HeaderItemController(
            onBackClickedAction = { Input.BackClicked.emit() }
    )
    private val kittenItemController = KittenItemController(
            onClickedAction = { Input.KittenClicked(it).emit() }
    )
    private val dividerItemController = DividerItemController(
            onRefreshClickedAction = { type ->
                when (type) {
                    DividerType.TOP_KITTEN -> Input.TopKittenUpdateClicked.emit()
                    DividerType.NEW_KITTIES -> Input.NewKittiesCountUpdateClicked.emit()
                    DividerType.POPULAR_KITTIES -> Input.PopularKittiesUpdateClicked.emit()
                }
            },
            onAllClickedAction = { type ->
                when (type) {
                    DividerType.POPULAR_KITTIES -> Input.PopularKittiesAllClicked.emit()
                    else -> Unit
                }
            }
    )

    @Inject
    override lateinit var hub: ScreenEventHub<KittiesEvent>

    @Inject
    lateinit var sh: KittiesStateHolder

    @Inject
    lateinit var ch: KittiesCommandsHolder

    @Inject
    lateinit var messageController: MessageController

    override fun getScreenName() = "KittiesActivityView"

    override fun getContentView() = R.layout.activity_kitties

    override fun createConfigurator() = KittiesScreenConfigurator(intent)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        initViews()
        bind()
    }

    private fun initViews() {
        kitties_rv.adapter = easyAdapter
        kitties_meow_btn.setOnClickListener { Input.MeowClicked.emit() }
    }

    private fun bind() {
        ch.kittenClicked bindTo ::displayKittenClickedMessage
        ch.updateSucceed bindTo ::displayUpdateSucceedMessage
        ch.updateFailed bindTo ::displayUpdateFailedMessage
        ch.meowSent bindTo ::displayMeowMessage
        sh bindTo ::render
    }

    private fun displayKittenClickedMessage() {
        messageController.show("Kitten clicked!")
    }

    private fun displayUpdateSucceedMessage() {
        messageController.show("Data loading succeed")
    }

    private fun displayUpdateFailedMessage() {
        messageController.show("Data loading failed")
    }

    private fun displayMeowMessage() {
        messageController.show("Meow!")
    }

    private fun render(state: KittiesState) {
        renderMeowButton(state)
        renderContent(state)
    }

    private fun renderMeowButton(state: KittiesState) {
        kitties_meow_btn_title_tv.text = "Meows: ${state.meowCount}"
        kitties_meow_btn_title_tv.isInvisible = state.isMeowButtonLoading
        kitties_meow_btn_progress_bar.isInvisible = !state.isMeowButtonLoading
    }

    private fun renderContent(state: KittiesState) {
        val topKittenDividerItem = DividerUi(
                type = DividerType.TOP_KITTEN,
                title = "Top kitten of the week!",
                isLoading = state.isTopKittenLoading
        )
        val newKittiesCountDividerItem = DividerUi(
                type = DividerType.NEW_KITTIES,
                title = "New kitties: ${state.newKittiesCount}",
                isLoading = state.isNewKittiesCountLoading
        )
        val popularKittiesDividerItem = DividerUi(
                type = DividerType.POPULAR_KITTIES,
                title = "Popular kitties",
                isLoading = state.isPopularKittiesLoading,
                isAllVisible = true
        )
        val itemList = ItemList.create()
                .add(headerItemController)
                .add(topKittenDividerItem, dividerItemController)
                .addIf(state.topKitten.isValid, state.topKitten, kittenItemController)
                .add(newKittiesCountDividerItem, dividerItemController)
                .add(popularKittiesDividerItem, dividerItemController)
                .addAll(state.popularKitties, kittenItemController)

        easyAdapter.setItems(itemList)
    }
}