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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.stub

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.mvp.binding.sample.R

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 */
fun View.toStub() {
    when (this) {
        is ViewGroup ->
            (0 until childCount)
                    .map { this.getChildAt(it) }
                    .forEach { it.toStub() }
        is TextView -> {
            this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.gray_light))
            this.text = null
        }
        is ImageView -> {
            this.setImageDrawable(null)
            this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.gray_light))
        }
    }
}