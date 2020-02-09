package ru.surfstudio.android.navigation.route.error

import java.lang.IllegalStateException

/**
 * Exception that occurs when screen class hasn't been declared at route.
 */
class RouteScreenNotDeclaredException
    : IllegalStateException("You haven't declared screen class in route! " +
        "Implement getScreenClass or getScreenClassPath method and try again")