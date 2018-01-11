package ru.surfstudio.standard.ui.util


import android.support.v4.view.ViewPager

/**
 * default page change listener
 */
class DefaultOnPageChangedListener : ViewPager.OnPageChangeListener {

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //do nothing
    }

    override fun onPageSelected(position: Int) {
        //do nothing
    }

    override fun onPageScrollStateChanged(state: Int) {
        //do nothing
    }
}
