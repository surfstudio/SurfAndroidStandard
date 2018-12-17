package ru.surfstudio.android.analyticsv2.core

/**
 * Представление действия в аналитике.
 * "Действие" идет без привязки к реализации конкретного сервиса аналитики,
 * т.е реализующая сущность может выполнить любое действия на конкретном api (Firebase, Flurry, AppMetrica...),
 * например, отправку события или установку свойств пользователя (setUserProperty)
 */
interface AnalyticAction