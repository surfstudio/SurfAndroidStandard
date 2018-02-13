package com.example.camera.exceptions

import com.example.camera.CameraModule
import com.example.camera.PhotoProvider
import dagger.Component
import ru.surfstudio.android.core.app.dagger.scope.PerApplication

/**
 * Created by vsokolova on 2/8/18.
 */
@PerApplication
@Component(modules = arrayOf(CameraModule::class))
interface CameraComponent {
    fun photoProvider(): PhotoProvider
}