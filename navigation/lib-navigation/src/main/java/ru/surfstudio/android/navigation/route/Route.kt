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
        val EXTRA_FIRST = "EXTRA_FIRST"
        val EXTRA_SECOND = "EXTRA_SECOND"
        val EXTRA_THIRD = "EXTRA_THIRD"
        val EXTRA_FOURTH = "EXTRA_FOURTH"
        val EXTRA_FIFTH = "EXTRA_FIFTH"
        val EXTRA_SIXTH = "EXTRA_SIXTH"
        val EXTRA_SEVEN = "EXTRA_SEVEN"
        val EXTRA_EIGHT = "EXTRA_EIGHT"
        val EXTRA_NINE = "EXTRA_NINE"
        val EXTRA_TEN = "EXTRA_TEN"
        val EXTRA_ELEVEN = "EXTRA_ELEVEN"
        val EXTRA_TWELVE = "EXTRA_TWELVE"
        val SCREEN_ID = "SCREEN_ID"
    }
}