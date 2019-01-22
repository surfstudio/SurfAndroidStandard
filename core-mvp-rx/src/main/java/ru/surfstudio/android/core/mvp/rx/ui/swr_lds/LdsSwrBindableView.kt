package ru.surfstudio.android.core.mvp.rx.ui.swr_lds

import ru.surfstudio.android.core.mvp.rx.ui.RxModel
import ru.surfstudio.android.core.mvp.rx.ui.lds.HasLoadState
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxView
import ru.surfstudio.android.core.mvp.rx.ui.swr.HasSwrState
import ru.surfstudio.android.core.mvp.rx.ui.swr.SwrRxView

interface LdsSwrBindableView<M> : LdsRxView<M>, SwrRxView<M>
        where M : RxModel,
              M : HasLoadState,
              M : HasSwrState