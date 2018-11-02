package ru.surfstudio.android.easyadapter.holder.async

/**
 * Определяет необходимые поля для работы асинхроного инфлейта у ViewHolder
 */
interface AsyncViewHolder {
    var isItemViewInflated: Boolean

    var fadeInDuration: Long
    var fadeInAction: () -> Unit

    var resizeDuration: Long
    var resizeAction: () -> Unit
}