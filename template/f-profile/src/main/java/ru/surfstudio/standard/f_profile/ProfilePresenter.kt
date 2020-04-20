package ru.surfstudio.standard.f_profile

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class ProfilePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
): BasePresenter<ProfileFragmentView>(basePresenterDependency) {

}
