package ru.surfstudio.android.shared.pref.sample.interactor.common.network.calladapter

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.calladapter.BaseCallAdapterFactory
import javax.inject.Inject

@PerApplication
class CallAdapterFactory @Inject constructor() : BaseCallAdapterFactory()