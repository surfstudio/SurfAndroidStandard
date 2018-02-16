package ru.surfstudio.android.recycler.extension.snaphelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * при флинге позволяет скроллить RecyclerView только на количество элементов, умещающееся в
 * видимой части списка
 */
public class PagerSnapHelper extends SnapHelper {
    private OrientationHelper mHorizontalHelper;

    public PagerSnapHelper(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenDensity = metrics.density;
    }

    private float screenDensity = 1;
    private int minVelocityDpX = 1800;

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        return new int[]{
                getHorizontalHelper(layoutManager).getDecoratedStart(targetView),
                0};
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (Math.abs(velocityX / screenDensity) < minVelocityDpX) {
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

    @NonNull
    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

}
