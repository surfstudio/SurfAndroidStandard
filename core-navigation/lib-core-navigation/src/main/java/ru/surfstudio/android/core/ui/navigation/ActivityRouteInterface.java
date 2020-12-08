/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.core.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;

/**
 * Navigation route interface with prepared Intent and {@link androidx.core.app.ActivityOptionsCompat}.
 *
 * Used for navigation between activities.
 * <br><br>
 * See: {@link Route}.
 * <br><br>
 */
public interface ActivityRouteInterface extends Route {

    /**
     * Prepared Intent for calling the target Activity and passing data.
     *
     * @param context activity context
     * @return prepared Intent
     */
    Intent prepareIntent(Context context);

    /**
     * Prepared {@link ActivityOptionsCompat} for setting up Activity transitions properties.
     *
     * @return prepared ActivityOptionsCompat
     */
    ActivityOptionsCompat prepareActivityOptionsCompat();

    /**
     * Prepared Bundle for passing data to the target Fragment.
     * Also used for passing shared-element view data to the following Activity in case of
     * shared-element transition implementation.
     *
     * @return prepared Bundle
     * @deprecated use {@link ActivityRouteInterface#prepareActivityOptionsCompat()} instead
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    Bundle prepareBundle();
}