package ru.surfstudio.standard.ui.navigation

import java.io.Serializable

/**
 * Типы табов на главном экране MainActivityView
 * TODO переименовать табы и добавить свои при необходимости
 */
enum class MainTabType: Serializable {
    FEED, // экран ленты
    SEARCH, // экран поиска
    PROFILE // экран профиля пользователя
}