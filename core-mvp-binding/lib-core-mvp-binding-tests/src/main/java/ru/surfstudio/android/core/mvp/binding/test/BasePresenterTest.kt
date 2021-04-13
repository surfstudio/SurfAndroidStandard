package ru.surfstudio.android.core.mvp.binding.test

import io.kotest.core.spec.style.AnnotationSpec
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.core.mvp.binding.test.navigation.TestActivityNavigator
import ru.surfstudio.android.core.mvp.binding.test.navigation.TestDialogNavigator
import ru.surfstudio.android.core.mvp.binding.test.navigation.TestFragmentNavigator
import ru.surfstudio.android.core.mvp.binding.test.navigation.TestTabFragmentNavigator
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

object TEST : ActionSource, ActionTarget, StateSource, StateTarget, CommandSource, CommandTarget

/**
 * Базовый класс для тестирования презентера
 * @param PT тип презентера
 * @param BMT тип bind-модели
 */
abstract class BasePresenterTest<PT : BaseRxPresenter, BMT : BindModel> : AnnotationSpec(), Related<TEST> {

    lateinit var presenter: PT
    lateinit var bm: BMT

    var activityNavigator = TestActivityNavigator()
    var fragmentNavigator = TestFragmentNavigator()
    var tabFragmentNavigator = TestTabFragmentNavigator()
    var dialogNavigator = TestDialogNavigator()

    var connectionProvider = TestConnectionProvider()

    var basePresenterDependency = createBasePresenterDependency()

    abstract fun createBindModel(): BMT

    abstract fun createPresenter(bm: BMT): PT

    fun createBasePresenterDependency(
        schedulersProvider: SchedulersProvider = TestSchedulersProvider(),
        screenState: ScreenState = TestScreenState(),
        eventDelegateManager: ScreenEventDelegateManager = TestScreenEventDelegateManager(),
        errorHandler: ErrorHandler = TestErrorHandler(),
        connectionProvider: ConnectionProvider = this.connectionProvider,
        activityNavigator: ActivityNavigator = this.activityNavigator
    ): BasePresenterDependency = BasePresenterDependency(
        schedulersProvider,
        screenState,
        eventDelegateManager,
        errorHandler,
        connectionProvider,
        activityNavigator
    )

    /**
     * Инициализация презентера
     *
     * Следует вызывать перед выполением каждого теста
     */
    fun setUpPresenter() {
        bm = createBindModel()
        presenter = createPresenter(bm)
        presenter.onFirstLoad()
    }

    /**
     * Востановление превоначального состояния окружения
     *
     * Следует вызывать после каждого теста
     */
    fun resetPresenter() {
        resetNavigators()
        connectionProvider.reset()
        presenter.onDestroy()
    }

    /**
     * Пересоздание презентера
     */
    fun recreatePresenter() {
        if (::presenter.isInitialized) {
            resetPresenter()
        }
        setUpPresenter()
    }

    fun resetNavigators() {
        activityNavigator.reset()
        fragmentNavigator.reset()
        tabFragmentNavigator.reset()
        dialogNavigator.reset()
    }

    override fun relationEntity() = TEST

    override fun <T> subscribe(
        observable: Observable<out T>,
        onNext: Consumer<T>,
        onError: (Throwable) -> Unit
    ): Disposable {
        throw NotImplementedError()
    }
}
