package ru.surfstudio.android.navigation.di.supplier.error

import java.lang.IllegalStateException

class SupplierNotInitializedError : IllegalStateException(
        "Navigation supplier isn't initialized. Have you implemented Navigation Supplier?"
)