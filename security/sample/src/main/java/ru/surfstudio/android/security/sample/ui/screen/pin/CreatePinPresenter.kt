package ru.surfstudio.android.security.sample.ui.screen.pin

import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import ru.surfstudio.android.biometrics.BiometricsService
import ru.surfstudio.android.biometrics.PromptInfo
import ru.surfstudio.android.biometrics.error.*
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.interactor.profile.ProfileInteractor
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider

/**
 * Презентер экрана ввода pin-кода
 */
@PerScreen
class CreatePinPresenter @Inject constructor(
    basePresenterDependency: BasePresenterDependency,
    private val route: CreatePinActivityRoute,
    private val resourceProvider: ResourceProvider,
    private val profileInteractor: ProfileInteractor,
    private val biometricsService: BiometricsService
) : BasePresenter<CreatePinActivityView>(basePresenterDependency) {

    private val screenModel = CreatePinScreenModel()
    private var biometricsDisposable = Disposables.disposed()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        checkBiometricsStatus()
    }

    fun submitPin(pin: String) {
        loadData(profileInteractor.signIn(route.apiKey, pin)) {
            val message = resourceProvider.getString(R.string.pin_created_message)
            view.showMessage(message)
        }
    }

    fun getApiKey(pin: String) {
        loadData(profileInteractor.getApiKey(pin)) {
            view.showMessage(it.apiKey)
        }
    }

    fun encryptPin(pin: String) {
        val promptInfo = PromptInfo(
            title = resourceProvider.getString(R.string.biometric_dialog_confirmation_title),
            subtitle = resourceProvider.getString(R.string.biometric_dialog_subtitle),
            negativeButtonText = resourceProvider.getString(R.string.biometric_dialog_cancel)
        )
        biometricsDisposable.dispose()
        biometricsDisposable = subscribe(biometricsService.encryptByBiometrics(pin, view, promptInfo),
            {
                view.render(screenModel.apply { encryptedPin = it })
            }, ::handleBiometricsError)
    }

    fun decryptPin(encryptedData: String) {
        val promptInfo = PromptInfo(
            title = resourceProvider.getString(R.string.biometric_dialog_activation_title),
            subtitle = resourceProvider.getString(R.string.biometric_dialog_subtitle),
            negativeButtonText = resourceProvider.getString(R.string.biometric_dialog_cancel)
        )
        biometricsDisposable.dispose()
        biometricsDisposable = subscribe(biometricsService.decryptByBiometrics(encryptedData, view, promptInfo),
            {
                view.showMessage(it)
            },
            ::handleBiometricsError)
    }

    private fun handleBiometricsError(error: Throwable) {
        Logger.e(error)
        val message = error.message ?: EMPTY_STRING
        when (error) {
            is CanceledException -> {
                // пользоваетль или система отменил сканер, ничего не показываем
            }
            is HardwareNotFoundException -> {
                // Эта ошибка не должна выходит, потому что мы сначало проверяем доступность биометрии
            }
            is HardwareUnavailableException -> {
                //Сканер временно не доступен, попробуйте позже
                view.showMessage(message)
            }
            is LockedOutException -> {
                //Сканер заблокирован на 30 секунд, так как пользователь сделал много попыток
                view.showMessage(message)
            }
            is LockoutPermanentException -> {
                //Очень много попыток, сканер полностью заблокирован,
                //нужно разблокировать в настройках приложения с помощью строгой аутентификации
                // (PIN-код / шаблон / пароль)
                view.showMessage(message)
            }
            is NegativeButtonException -> {
                // пользователь нажал кнопку отмена в диалоге сканера, ничего не делаем
            }
            is NoBiometricsRegisteredException -> {
                //на устройстве отсутствует зарегистрированной биометрии,
                //эта ошибка не должна выйти, так как мы сначало проверяем
            }
            is NoDeviceCredential -> {
                //на устройстве отсутствует зарегистрированного пинкода,
                //эта ошибка не должна выйти, так как мы сначало проверяем
            }
            is NoSpaceLeftException -> {
                //недостаточно места для выполнения операции
                view.showMessage(message)
            }
            is TimeoutException -> {
                //диалог отпечатки пальцев простоял долгое время
                //ничего не делаем
            }
            is VendorErrorException -> {
                //ошибка производителя
                view.showMessage(message)
            }
            else -> {
                view.showMessage(message)
            }
        }
    }

    private fun checkBiometricsStatus() {
        view.render(
            screenModel.apply {
                isBiometricsAvailable = biometricsService.hasBiometricsRegistered()
            }
        )
    }

    private fun <T> loadData(observable: Observable<T>,
                             onNext: (T) -> Unit) {
        subscribeIoHandleError(observable, {
            onNext(it)
            view.render(screenModel)
        }, {
            view.render(screenModel)
        })
    }
}
