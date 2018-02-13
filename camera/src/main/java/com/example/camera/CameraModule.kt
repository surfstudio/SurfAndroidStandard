package com.example.camera

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.util.ActiveActivityHolder

/**
 * Created by vsokolova on 2/8/18.
 */
@Module
class CameraModule(private val activeActivityHolder: ActiveActivityHolder,
                   private val cameraStoragePermissionChecker: CameraStoragePermissionChecker) {

    @Provides
    fun provideActiveActivityHolder(): ActiveActivityHolder = activeActivityHolder

    @Provides
    fun provideCameraStoragePermissionChecker(): CameraStoragePermissionChecker = cameraStoragePermissionChecker


}