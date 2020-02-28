package ru.surfstudio.android.picturechooser

import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.provider.ActivityProvider

/**
 * Позволяет получить одно или несколько изображений из любого места на устройстве
 */
class ChooserPictureProvider(
        private val activityNavigator: ActivityNavigator,
        private val activityProvider: ActivityProvider
) {

    private val currentActivity get() = activityProvider.get()

    //region Функции для выбора одного изображения
    fun createChooserForSingleImage(message: String): Observable<String> {
        val route = ChooserSingleImageRoute(message)
        val result = observeSingleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun createChooserForSingleImageUri(message: String): Observable<String> {
        val route = ChooserSingleImageUriRoute(message)
        val result = observeSingleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun createChooserForSingleImageUriWrapper(message: String): Observable<UriWrapper> {
        val route = ChooserSingleImageUriWrapperRoute(message)
        val result = observeSingleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }
    //endregion

    //region Функции для выбора нескольких изображений
    fun createChooserForMultipleImage(message: String): Observable<List<String>> {
        val route = ChooserMultipleImageRoute(message)
        val result = observeMultipleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun createChooserForMultipleImageUri(message: String): Observable<List<String>> {
        val route = ChooserMultipleImageUriRoute(message)
        val result = observeMultipleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun createChooserForMultipleImageUriWrapper(message: String): Observable<List<UriWrapper>> {
        val route = ChooserMultipleImageUriWrapperRoute(message)
        val result = observeMultipleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }
    //endregion

    //region Маршруты для выбора одного изображения
    /**
     * Маршрут, возвращащий путь к изображению
     */
    private inner class ChooserSingleImageRoute(
            private val chooserMessage: String
    ) : ActivityWithResultRoute<String>() {

        override fun prepareIntent(context: Context?) =
                createChooserIntentForSingleImage(chooserMessage)

        override fun parseResultIntent(intent: Intent?): String? =
                parseSingleResultIntent(intent) { it.getRealPath(currentActivity) }
    }

    /**
     * Маршрут, возвращающий Uri изображения, преобразованный в String
     */
    private inner class ChooserSingleImageUriRoute(
            private val chooserMessage: String
    ) : ActivityWithResultRoute<String>() {

        override fun prepareIntent(context: Context?) =
                createChooserIntentForSingleImage(chooserMessage)

        override fun parseResultIntent(intent: Intent?): String? =
                parseSingleResultIntent(intent) { it.toString() }
    }

    /**
     * Маршрут, возвращающий класс-обертку над Uri изображения
     */
    private inner class ChooserSingleImageUriWrapperRoute(
            private val chooserMessage: String
    ) : ActivityWithResultRoute<UriWrapper>() {

        override fun prepareIntent(context: Context?) =
                createChooserIntentForSingleImage(chooserMessage)

        override fun parseResultIntent(intent: Intent?): UriWrapper? =
                parseSingleResultIntent(intent) { UriWrapper(it) }
    }
    //endregion

    //region Маршруты для выбора нескольких изображений
    /**
     * Маршрут, возвращающий список путей к выбранным изображениям
     */
    private inner class ChooserMultipleImageRoute(
            private val chooserMessage: String
    ) : ActivityWithResultRoute<ArrayList<String>>() {

        override fun prepareIntent(context: Context?) =
                createChooserIntentForMultipleImage(chooserMessage)

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? {
            return parseMultipleResultIntent(intent) { it.getRealPaths(currentActivity) }
        }
    }

    /**
     * Маршрут, возвращающий список Uri выбранных изображений, преобразованных в String
     */
    private inner class ChooserMultipleImageUriRoute(
            private val chooserMessage: String
    ) : ActivityWithResultRoute<ArrayList<String>>() {

        override fun prepareIntent(context: Context?) =
                createChooserIntentForMultipleImage(chooserMessage)

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? =
                parseMultipleResultIntent(intent) { it.toStringArrayList() }
    }

    /**
     * Маршрут, возвращающий список элементов типа класса-обертки над Uri выбранных изображений
     */
    private inner class ChooserMultipleImageUriWrapperRoute(
            private val chooserMessage: String
    ) : ActivityWithResultRoute<ArrayList<UriWrapper>>() {

        override fun prepareIntent(context: Context?) =
                createChooserIntentForMultipleImage(chooserMessage)

        override fun parseResultIntent(intent: Intent?): ArrayList<UriWrapper>? =
                parseMultipleResultIntent(intent) { it.toUriWrapperList() }
    }
    //endregion

    //region Вспомогательные функции для маршрутов
    private fun createChooserIntentForSingleImage(message: String): Intent =
            createChooser(getIntentForSingleImageFromGallery(), message)

    private fun createChooserIntentForMultipleImage(message: String): Intent =
            createChooser(getIntentForMultipleImageFromGallery(), message)
    //endregion
}