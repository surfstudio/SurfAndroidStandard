package ru.surfstudio.android.navigation.navigator.activity.view

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator

data class ViewActivityNavigator(override val activity: AppCompatActivity) : ActivityNavigator()