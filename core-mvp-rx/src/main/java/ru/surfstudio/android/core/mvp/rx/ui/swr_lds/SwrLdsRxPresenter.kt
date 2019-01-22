package ru.surfstudio.android.core.mvp.rx.ui.swr_lds

import ru.surfstudio.android.core.mvp.rx.ui.CoreRxPresenter
import ru.surfstudio.android.core.mvp.rx.ui.RxModel
import ru.surfstudio.android.core.mvp.rx.ui.lds.HasLoadState
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxPresenter
import ru.surfstudio.android.core.mvp.rx.ui.swr.HasSwrState
import ru.surfstudio.android.core.mvp.rx.ui.swr.SwrRxModel
import ru.surfstudio.android.core.mvp.rx.ui.swr.SwrRxPresenter

interface SwrLdsRxPresenter<M> : SwrRxPresenter<M>, LdsRxPresenter<M>
        where M : RxModel, M : HasLoadState, M : HasSwrState