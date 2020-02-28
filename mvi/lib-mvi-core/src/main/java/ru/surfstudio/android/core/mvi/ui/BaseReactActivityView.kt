package ru.surfstudio.android.core.mvi.ui

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView

abstract class BaseReactActivityView : BaseRxActivityView(), BaseReactView

abstract class BaseReactFragmentView : BaseRxFragmentView(), BaseReactView