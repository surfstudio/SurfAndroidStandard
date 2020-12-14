package ru.surfstudio.standard.ui.mvi.navigation.event

import ru.surfstudio.standard.ui.mvi.composition.SingleEventComposition

/**
 * Композиция событий навигации на основе команд
 */
interface NavCommandsComposition : SingleEventComposition<NavCommandsEvent>
