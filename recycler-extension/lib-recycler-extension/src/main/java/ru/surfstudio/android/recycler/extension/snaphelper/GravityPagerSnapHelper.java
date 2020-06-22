/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.recycler.extension.snaphelper;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Комбинирует поведение снапига по гравитации и запрета на "бесконечный" скролл
 */
public class GravityPagerSnapHelper extends GravitySnapHelper {

    private static final int MIN_VELOCITY_DP_X = 1800;
    private float screenDensity;

    public GravityPagerSnapHelper(int gravity, Context context) {
        super(gravity);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenDensity = metrics.density;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (Math.abs(velocityX / screenDensity) < MIN_VELOCITY_DP_X) {
            return RecyclerView.NO_POSITION;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int fistVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        int delta = lastVisibleItemPosition - fistVisibleItemPosition;
        int targetPosition;
        final int firstItem = 0;
        final int lastItem = layoutManager.getItemCount() - 1;
        if (velocityX > 0) {
            targetPosition = lastVisibleItemPosition;
            return Math.min(lastItem, targetPosition);
        } else {
            targetPosition = fistVisibleItemPosition - delta + 1;
            return Math.max(firstItem, targetPosition);
        }
    }
}
