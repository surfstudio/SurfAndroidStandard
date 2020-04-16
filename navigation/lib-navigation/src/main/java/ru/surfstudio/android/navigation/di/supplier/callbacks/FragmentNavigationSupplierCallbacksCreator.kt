package ru.surfstudio.android.navigation.di.supplier.callbacks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

typealias FragmentNavigationSupplierCallbacksCreator = (AppCompatActivity, Bundle?) -> FragmentNavigationSupplierCallbacks