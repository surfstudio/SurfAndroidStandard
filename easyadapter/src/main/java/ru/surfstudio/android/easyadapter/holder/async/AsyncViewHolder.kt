package ru.surfstudio.android.easyadapter.holder.async

import android.view.View
import android.view.ViewGroup

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
     * Определяет длительность анимации изменения размеров
     */
    var resizeDuration: Long

    val holderKey: String
        get() = this::class.java.name

    /**
     * Определяет функцию, которая сразобает после инфлейта основной вью
     */
    fun onViewInflated(view: View) {}

    /**
     * Определяет функцию, которая будет выполнена после появления основной вью
     */
    fun onFadeInEnd() {}

    fun toSize(itemView: ViewGroup, oldHeight: Int, height: Int) = resize(itemView, oldHeight, height)
}