package ru.surfstudio.android.navigation.route

/**
 * ## Base navigation route interface.
 *
 * Route is responsible for the following tasks:
 *
 * ### Mandatory.
 * * resolving the target screen
 * ### Optional.
 * * passing data between screens using Intent (Bundle);
 * * retrieving passed data from Intent (Bundle);
 * * sending data back to the previous screen;
 * * retrieving delivered data on the previous screen;
 * * uniquely identify the screen;
 *
 * Route is able to pass up to 12 data items marked with one of the built-in "EXTRA_NUMBERED"
 * string markers.
 *
 * @see BaseRoute
 */
interface Route {
    companion object {
        const val EXTRA_FIRST = "EXTRA_FIRST"
        const val EXTRA_SECOND = "EXTRA_SECOND"
        const val EXTRA_THIRD = "EXTRA_THIRD"
        const val EXTRA_FOURTH = "EXTRA_FOURTH"
        const val EXTRA_FIFTH = "EXTRA_FIFTH"
        const val EXTRA_SIXTH = "EXTRA_SIXTH"
        const val EXTRA_SEVEN = "EXTRA_SEVEN"
        const val EXTRA_EIGHT = "EXTRA_EIGHT"
        const val EXTRA_NINE = "EXTRA_NINE"
        const val EXTRA_TEN = "EXTRA_TEN"
        const val EXTRA_ELEVEN = "EXTRA_ELEVEN"
        const val EXTRA_TWELVE = "EXTRA_TWELVE"

        const val EXTRA_SCREEN_ID = "ROUTE_EXTRA_SCREEN_ID"
    }
}