package ru.surfstudio.android.easyadapter.holder.async

import android.view.View

/**
 * Определяет необходимые поля для работы асинхроного инфлейта у ViewHolder
 */
interface AsyncViewHolder {
    /**
     * Показывает был ли выполнен инфлейт основной вью
     */
    var isItemViewInflated: Boolean

    /**
     * Определяет длительность анимации появления
     */
    var fadeInDuration: Long

    /**
     * Определяет функцию, которая сразобает после инфлейта основной вью
     */
    fun onViewInflated(view: View) {}

    /**
     * Определяет функцию, которая будет выполнена после появления основной вью
     */
    fun onFadeInEnd() {}
}