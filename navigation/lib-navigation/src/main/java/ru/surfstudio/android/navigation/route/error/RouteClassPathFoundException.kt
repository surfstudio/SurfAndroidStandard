package ru.surfstudio.android.navigation.route.error

import java.lang.IllegalStateException

/**
 * Exception that occurs when screen cant be found on declared class path
 */
class RouteClassPathFoundException(classPath: String?) : IllegalStateException(
        "Screen with the following classpath was not found in the current " +
                "application: $classPath. If this screen is the part of Dynamic Feature, " +
                "please check if this Dynamic Feature is downloaded and installed on the device" +
                "successfully."
)