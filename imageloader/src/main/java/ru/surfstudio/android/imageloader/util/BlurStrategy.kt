package ru.surfstudio.android.imageloader.util

/**
 * Стратегия размытия изображения
 */
enum class BlurStrategy {
    STACK_BLUR,   //Размытие методом Stack Blur
    RENDER_SCRIPT //Размытие с помощью Android Render Script
}