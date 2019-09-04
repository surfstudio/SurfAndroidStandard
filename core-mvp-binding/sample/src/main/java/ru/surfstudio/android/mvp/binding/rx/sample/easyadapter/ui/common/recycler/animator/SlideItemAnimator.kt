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

import android.view.animation.*
import androidx.recyclerview.widget.RecyclerView

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 */
class SlideItemAnimator : StandardItemAnimator() {

    override fun onRemoveStartingInternal(item: RecyclerView.ViewHolder) {
        super.onRemoveStartingInternal(item)
        val anim = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f)
        anim.interpolator = AccelerateInterpolator()
        anim.duration = removeDuration
        item.itemView.startAnimation(anim)
    }

    override fun onAddStartingInternal(item: RecyclerView.ViewHolder) {
        super.onAddStartingInternal(item)
        val yAnim = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1 / 8f,
                Animation.RELATIVE_TO_SELF, 0f)
        val alphaAnim = AlphaAnimation(0f, 1f)
        val animSet = AnimationSet(false)
        animSet.interpolator = DecelerateInterpolator()
        animSet.duration = addDuration
        animSet.addAnimation(yAnim)
        animSet.addAnimation(alphaAnim)
        item.itemView.startAnimation(animSet)
    }
}
