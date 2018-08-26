package ru.surfstudio.android.location.sample

import android.location.Location
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.location.DefaultLocationInteractor
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution
import javax.inject.Inject

@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        screenEventDelegateManager: ScreenEventDelegateManager,
        activityProvider: ActivityProvider,
        permissionManager: PermissionManager,
        private val locationService: LocationService,
        private val defaultLocationInteractor: DefaultLocationInteractor
) : BasePresenter<CoreView>(basePresenterDependency) {

    private val resolution1 = NoLocationPermissionResolution(permissionManager)
    private val resolution2 = PlayServicesAreNotAvailableResolution(screenEventDelegateManager, activityProvider)
    private val resolution3 = ResolvableApiExceptionResolution(screenEventDelegateManager, activityProvider)

    init {
        /**
         * [PlayServicesAreNotAvailableResolution] и [ResolvableApiExceptionResolution] требуют регистрации экземпляра
         * их внутреннего класса [ActivityResulDelegate], чтобы получать результаты запросов.
         */
        screenEventDelegateManager.registerDelegate(resolution2.ResolutionActivityResultDelegate())
        screenEventDelegateManager.registerDelegate(resolution3.ResolutionActivityResultDelegate())
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        /**
         * Проверить возможность получения местоположения.
         *
         * Возвращается список исключений, связанных с невозможностью получения местоположения.
         * Возможные исключения:
         * - [NoLocationPermissionException]
         * - [PlayServicesAreNotAvailableException]
         */
        val locationAvailabilityExceptions: List<Exception> = locationService.checkLocationAvailability()

        /**
         * Если список пуст - значит есть возможность получить локацию.
         */
        if (locationAvailabilityExceptions.isEmpty()) {

            /**
             * Запросить последнее известное местоположение.
             *
             * Принимает в качестве параметра массив решений проблем связанных с невозможностью получения
             * местоположения. Доступные решения:
             * - [NoLocationPermissionResolution]
             * - [PlayServicesAreNotAvailableResolution]
             * - [ResolvableApiExceptionResolution]
             */
            val lastKnownLocationMaybe: Maybe<Location> =
                    locationService.observeLastKnownLocation(resolution1, resolution2)

            lastKnownLocationMaybe.subscribe(
                    object : MaybeObserver<Location> {

                        override fun onSuccess(location: Location) {
                            // Вызывается в случае удачного получения локации.
                        }

                        override fun onComplete() {
                            // Вызывается в случае, если локация была получена, но равна null.
                        }

                        override fun onSubscribe(d: Disposable?) {
                        }

                        override fun onError(e: Throwable?) {
                            /**
                             * Вызывается в случае ошибки.
                             *
                             * Могут прийти следующие исключения:
                             *
                             * - [CompositeException], содержащий список из возможных исключений:
                             * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                             * [ResolvableApiException]
                             *
                             * - [ResolutionFailedException], если передавались экземпляры решений и попытка решения не
                             * удалась
                             */
                        }
                    }
            )

            /**
             * Подписаться на получение обновлений местоположения.
             *
             * Поток бесконечный.
             *
             * Принимает в качестве параметров:
             * - интервал в миллисекундах, при котором предпочтительно получать обновления местоположения;
             * - максимальный интервал в миллисекундах, при котором возможно обрабатывать обновления местоположения;
             * - приоритет запроса (точность метостоположения/заряд батареи);
             * - массив решений проблем связанных с невозможностью получения местоположения (аналогично
             * LocationService.observeLastKnownLocation())
             *
             * Метод onError() аналогичен LocationService.observeLastKnownLocation().
             */
            val locationUpdatesObservable: Observable<Location> =
                    locationService.observeLocationUpdates(1000, 500, LocationPriority.LOW_POWER, resolution3)


            /**
             * Аналогично LocationService.observeLastKnownLocation() с передачей всех решений.
             */
            defaultLocationInteractor.observeLastKnownLocation()

            /**
             * Запросить текущее местоположение. Аналогично LocationService.observeLocationUpdates() с передачей всех
             * решений и получением только первого не-null местоположения.
             *
             * Принимает в качестве параметра таймаут, при котором последнее полученное местоположение актуально (по
             * умолчанию 5 секунд)
             */
            val currentLocationSingle: Single<Location> = defaultLocationInteractor.observeCurrentLocation(1000)
        }
    }
}