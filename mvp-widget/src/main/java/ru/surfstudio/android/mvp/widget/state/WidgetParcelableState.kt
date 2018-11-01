package ru.surfstudio.android.mvp.widget.state

import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.AbsSavedState

/**
 * Стейт виджета
 * todo пока что оставил, но сейчас не используем рестор/сейв стейт у вью
 */
class WidgetParcelableState : AbsSavedState {

    lateinit var viewId: String

    constructor(parcel: Parcel) : super(parcel)

    constructor(parcel: Parcel, loader: ClassLoader) : super(parcel, loader) {
        this.viewId = parcel.readString()!!
    }

    constructor(superState: Parcelable, viewId: String) : super(superState) {
        this.viewId = viewId
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(viewId)
    }

    companion object CREATOR : Parcelable.Creator<WidgetParcelableState> {
        override fun createFromParcel(parcel: Parcel): WidgetParcelableState {
            return WidgetParcelableState(parcel)
        }

        override fun newArray(size: Int): Array<WidgetParcelableState?> {
            return arrayOfNulls(size)
        }
    }
}