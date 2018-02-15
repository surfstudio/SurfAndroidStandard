package com.example.camera.exceptions

import com.example.camera.CameraModule
import com.example.camera.PhotoProvider
import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication

/**
 * Created by vsokolova on 2/8/18.
 */
@PerApplication
@Component(modules = [CameraModule::class])
interface CameraComponent {
    fun photoProvider(): PhotoProvider
}