/*
  Copyright (c) 2020-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.recycler.extension.animator

/**
 * Base interface for view holders which support animations.
 * Can be used with [BaseItemAnimator]
 */
interface AnimatableViewHolder {

    /**
     * Override this method, if you want custom animation "insert" appear for this holder
     *
     * @return true, if holder has custom animation
     */
    fun animateInsert(): Boolean {
        return false
    }

    /**
     * Override this method, if you want custom animation "change" appear for this holder
     *
     * @return true, if holder has custom animation
     */
    fun animateChange(): Boolean {
        return false
    }

    /**
     * Override this method, if you want custom animation "remove" appear for this holder
     *
     * @return true, if holder has custom animation
     */
    fun animateRemove(): Boolean {
        return false
    }
}