package ru.surfstudio.standard.ui.mvi.navigation.event

import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.navigation.command.NavigationCommand

/**
 * Событие, содержащее в себе список команд навигации.
 */
data class NavCommandsEvent(val commands: List<NavigationCommand> = listOf()) : NavigationEvent
