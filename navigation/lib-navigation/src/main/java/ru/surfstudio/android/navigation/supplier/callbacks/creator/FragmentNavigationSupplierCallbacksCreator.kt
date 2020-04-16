package ru.surfstudio.android.navigation.supplier.callbacks.creator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.supplier.callbacks.FragmentNavigationSupplierCallbacks

typealias FragmentNavigationSupplierCallbacksCreator = (AppCompatActivity, Bundle?) -> FragmentNavigationSupplierCallbacks