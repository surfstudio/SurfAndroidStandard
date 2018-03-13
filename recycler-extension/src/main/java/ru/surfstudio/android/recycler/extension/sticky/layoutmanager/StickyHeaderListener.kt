package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import android.view.View

interface StickyHeaderListener {

    /**
     * Called when a Sticky Header has been attached or rebound.
     *
     * @param headerView      The view that is currently attached as the sticky header
     * @param adapterPosition The position in the adapter data set that this view represents
     */
    fun headerAttached(headerView: View, adapterPosition: Int)

    /**
     * Called when a Sticky Header has been detached or is about to be re-bound.
     *
     *
     * For performance reasons, if the new Sticky Header that will be replacing the current one is
     * of the same view type, the view is reused. In that case, this call will be immediately followed
     * by a call to [StickyHeaderListener.headerAttached] with the same view instance,
     * but after the view is re-bound with the new adapter data.
     *
     *
     * **Important**<br></br>
     * `adapterPosition` cannot be guaranteed to be the position in the current adapter
     * data set that this view represents. The data may have changed after this view was bound, but
     * before it was detached.
     *
     *
     * It is also possible for `adapterPosition` to be -1, though this should be a rare case.
     *
     *
     * In short, be wary about relying on the adapter position provided by this callback unless you are
     * working with a data set that is completely under your control (no user-initiated changes).
     *
     * @param headerView      The view that will be removed from the sticky header position, or soon to be re-bound
     * @param adapterPosition The position in the adapter data set that the header view was created from when originally bound
     */
    fun headerDetached(headerView: View, adapterPosition: Int)
}
