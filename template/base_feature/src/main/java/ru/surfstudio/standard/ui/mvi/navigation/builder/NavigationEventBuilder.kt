package ru.surfstudio.standard.ui.mvi.navigation.builder

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsComposition
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent

/**
 * Класс для удобного составления списка команд навигации для события Navigation.
 */
class NavigationEventBuilder<T : NavCommandsComposition>(private val navigationComposition: T) {

    private val commandList = mutableListOf<NavigationCommand>()

    /**
     * Добавление команды навигации.
     *
     * @return [NavigationEventBuilder].
     */
    fun add(command: NavigationCommand): NavigationEventBuilder<T> =
            this.apply { commandList.add(command) }

    /**
     * Добавление коллекции, содержащей команды навигации.
     *
     * @return [NavigationEventBuilder].
     */
    fun addAll(commands: Collection<NavigationCommand>): NavigationEventBuilder<T> =
            this.apply { commandList.addAll(commands) }

    /**
     * Добавление всех команд в [NavigationComposition].
     *
     * @return [NavigationComposition].
     */
    fun build(): T = navigationComposition.apply { event = NavCommandsEvent(commandList) }
}
