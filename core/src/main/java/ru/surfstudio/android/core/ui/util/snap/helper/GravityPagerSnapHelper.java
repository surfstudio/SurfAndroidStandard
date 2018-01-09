package ru.surfstudio.android.core.ui.util.snap.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Комбинирует поведение снапига по гравитации и запрета на "бесконечный" скролл
 */
public class GravityPagerSnapHelper extends GravitySnapHelper {

    private PagerSnapHelper pagerSnapHelper;

    public GravityPagerSnapHelper(int gravity, Context context) {
        super(gravity);
        pagerSnapHelper = new PagerSnapHelper(context);
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        return pagerSnapHelper.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    }
}
