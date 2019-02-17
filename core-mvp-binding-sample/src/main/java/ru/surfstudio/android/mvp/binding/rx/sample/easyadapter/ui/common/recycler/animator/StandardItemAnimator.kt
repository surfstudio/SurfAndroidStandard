/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.recycler.animator

import ru.surfstudio.android.easyadapter.animator.BaseItemAnimator

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 */
open class StandardItemAnimator : BaseItemAnimator() {
    private val ADD_DURATION: Long = 200
    private val REMOVE_DURATION: Long = 350
    private val MOVE_DURATION: Long = 350
    private val CHANGE_DURATION: Long = 200

    init {
        addDuration = ADD_DURATION
        removeDuration = REMOVE_DURATION
        moveDuration = MOVE_DURATION
        changeDuration = CHANGE_DURATION
        supportsChangeAnimations = false
    }
}
