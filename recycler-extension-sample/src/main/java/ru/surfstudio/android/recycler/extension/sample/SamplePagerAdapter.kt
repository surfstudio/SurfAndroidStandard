package ru.surfstudio.android.recycler.extension.sample

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class SamplePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
            SampleFragment::class.java.newInstance().apply { arguments = Bundle(1).apply { putString(EXTRA, "$position") } }


    override fun getCount(): Int = 200

    override fun saveState(): Parcelable? = null

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) { }

}