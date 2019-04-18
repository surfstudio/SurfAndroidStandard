/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin.

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
package ru.surfstudio.android.activity.holder

import android.app.Activity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Содержит активную (отображаемую) активти
 */
class ActiveActivityHolder {

    private val activitySubject = PublishSubject.create<Activity>()
    val activityObservable = activitySubject.hide()

    var activity: Activity? = null
        set(value) {
            field = value
            activitySubject.onNext(value ?: return)
        }

    val isExist: Boolean
        get() = activity != null

    fun clearActivity() {
        this.activity = null
    }
}