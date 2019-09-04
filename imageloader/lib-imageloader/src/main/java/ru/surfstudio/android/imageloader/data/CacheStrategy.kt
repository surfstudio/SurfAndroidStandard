package ru.surfstudio.android.imageloader.data

/**
 * Стратегия кеширования
 */
enum class CacheStrategy {
    CACHE_ORIGINAL, //Кеширование только оригинального изображения
    CACHE_TRANSFORMED, //Кеширование финальной измененной версии изображения
    CACHE_ALL, //Кеширование всех версий
    CACHE_NOTHING, //Отсутствие кеширования
    CACHE_AUTO //Автоматический выбор стратегии
}