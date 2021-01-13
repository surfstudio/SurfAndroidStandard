package ru.surfstudio.standard.ui.mvi.navigation.extension

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.animation.resource.NoResourceAnimations
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.activity.Finish
import ru.surfstudio.android.navigation.command.activity.FinishAffinity
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.command.activity.StartForSystemResult
import ru.surfstudio.android.navigation.command.dialog.Dismiss
import ru.surfstudio.android.navigation.command.dialog.Show
import ru.surfstudio.android.navigation.command.fragment.*
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.route.BaseRoute
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.route.dialog.DialogRoute
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.result.ResultRoute
import ru.surfstudio.android.navigation.route.result.SystemActivityResultRoute
import ru.surfstudio.standard.ui.mvi.navigation.builder.NavigationEventBuilder
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsComposition
import ru.surfstudio.standard.ui.mvi.navigation.event.NavCommandsEvent
import java.io.Serializable

/**
 * Создание билдера команд навигации для [NavCommandsComposition].
 */
fun <T : NavCommandsComposition> T.builder() = NavigationEventBuilder(this)

/**
 * Закрытие активити
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.finish(): NavigationEventBuilder<T> =
        add(
                Finish()
        )

/**
 * Эмит результата работы экрана
 */
fun <N, T, R> NavigationEventBuilder<N>.emitResult(route: R, result: T): NavigationEventBuilder<N>
        where N : NavCommandsComposition,
              T : Serializable,
              R : BaseRoute<*>,
              R : ResultRoute<T> {
    return add(
            EmitScreenResult(route, result)
    )
}

/** см [NavigationEventBuilder.finish] */
fun <T : NavCommandsComposition> T.finish(): T {
    return createSingleCommandComposition(Finish())
}

/**
 * Закрытие таска активити
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.finishAffinity(): NavigationEventBuilder<T> =
        add(
                FinishAffinity()
        )

/** см [NavigationEventBuilder.finishAffinity] */
fun <T : NavCommandsComposition> T.finishAffinity(): T {
    return createSingleCommandComposition(FinishAffinity())
}

/**
 * Открытие активити
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.start(route: ActivityRoute): NavigationEventBuilder<T> =
        add(
                Start(route)
        )

/** см [NavigationEventBuilder.start] */
fun <T : NavCommandsComposition> T.start(
        route: ActivityRoute,
        animations: Animations = DefaultAnimations.activity
): T {
    return createSingleCommandComposition(Start(route, animations))
}

/**
 * Открытие активити с результатом
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.startForSystemResult(route: SystemActivityResultRoute<*>): NavigationEventBuilder<T> =
        add(
                StartForSystemResult(route)
        )

/** см [NavigationEventBuilder.startForSystemResult] */
fun <T : NavCommandsComposition> T.startForSystemResult(
        route: SystemActivityResultRoute<*>,
        animations: Animations = DefaultAnimations.activity
): T {
    return createSingleCommandComposition(StartForSystemResult(route, animations))
}

/**
 * Закрытие текущей Activity и открытие [route].
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.replace(route: ActivityRoute): NavigationEventBuilder<T> =
        add(
                ru.surfstudio.android.navigation.command.activity.Replace(route)
        )

/** см [NavigationEventBuilder.replace] */
fun <T : NavCommandsComposition> T.replace(route: ActivityRoute): T {
    return createSingleCommandComposition(
            ru.surfstudio.android.navigation.command.activity.Replace(route)
    )
}

/**
 * Замена текущего фрагмента в стеке с сохранением его в бекстек.
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.replace(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): NavigationEventBuilder<T> =
        add(
                Replace(route, animations, sourceTag)
        )

/** см [NavigationEventBuilder.replace] */
fun <T : NavCommandsComposition> T.replace(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): T = createSingleCommandComposition(Replace(route, animations, sourceTag))

/**
 * Замена фрагмента в стеке и явное удаление текущего из бекстека.
 *
 * Вызов этой команды эквивалентен вызову RemoveLast + Add за одну операцию.
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.replaceHard(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): NavigationEventBuilder<T> =
        add(
                ReplaceHard(route, animations, sourceTag)
        )

/** см [NavigationEventBuilder.replaceHard] */
fun <T : NavCommandsComposition> T.replaceHard(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): T = createSingleCommandComposition(ReplaceHard(route, animations, sourceTag))

/**
 * Добавление фрагмента в стек поверх текущего.
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.add(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): NavigationEventBuilder<T> =
        add(
                Add(route, animations, sourceTag)
        )

/** см [NavigationEventBuilder.add] */
fun <T : NavCommandsComposition> T.add(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): T = createSingleCommandComposition(Add(route, animations, sourceTag))

/**
 * Удаление фрагмента из стека
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.remove(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): NavigationEventBuilder<T> =
        add(
                Remove(route, animations, sourceTag)
        )

/** см [NavigationEventBuilder.remove] */
fun <T : NavCommandsComposition> T.remove(
        route: FragmentRoute,
        animations: Animations = DefaultAnimations.fragment,
        sourceTag: String = ""
): T = createSingleCommandComposition(Remove(route, animations, sourceTag))

/**
 * Удаление последнего фрагмента из стека
 *
 * @param isTab параметр, показывающий, из какого стека удаляем:
 * из таска TabFragmentNavigator, или обычного FragmentNavigator
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.removeLast(
        animations: Animations = NoResourceAnimations,
        sourceTag: String = ""
): NavigationEventBuilder<T> =
        add(
                RemoveLast(animations, sourceTag)
        )

/** см [NavigationEventBuilder.removeLast] */
fun <T : NavCommandsComposition> T.removeLast(
        animations: Animations = NoResourceAnimations,
        sourceTag: String = ""
): T = createSingleCommandComposition(RemoveLast(animations, sourceTag))

/**
 * Удаление всех фрагментов из стека
 *
 * @param isTab параметр, показывающий, из какого стека удаляем:
 * из таска TabFragmentNavigator, или обычного FragmentNavigator
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.removeAll(
        animations: Animations = NoResourceAnimations,
        sourceTag: String = "",
        shouldRemoveLast: Boolean = false
): NavigationEventBuilder<T> =
        add(
                RemoveAll(animations, sourceTag, shouldRemoveLast)
        )

/** см [NavigationEventBuilder.removeAll] */
fun <T : NavCommandsComposition> T.removeAll(
        animations: Animations = NoResourceAnimations,
        sourceTag: String = "",
        shouldRemoveLast: Boolean = false
): T = createSingleCommandComposition(RemoveAll(animations, sourceTag, shouldRemoveLast))

/**
 * Удаление фрагментов из стека, пока не встретится [route].
 *
 * @param isInclusive следует ли удалять переданный в параметре route
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.removeUntil(
        route: FragmentRoute,
        animations: Animations = NoResourceAnimations,
        sourceTag: String = "",
        isInclusive: Boolean
): NavigationEventBuilder<T> =
        add(
                RemoveUntil(route, animations, sourceTag, isInclusive)
        )

/** см [NavigationEventBuilder.removeUntil] */
fun <T : NavCommandsComposition> T.removeUntil(
        route: FragmentRoute,
        animations: Animations = NoResourceAnimations,
        sourceTag: String = "",
        isInclusive: Boolean
): T = createSingleCommandComposition(RemoveUntil(route, animations, sourceTag, isInclusive))

/**
 * Показ диалога
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.show(
        route: DialogRoute
): NavigationEventBuilder<T> = add(Show(route))

/** см [NavigationEventBuilder.show] */
fun <T : NavCommandsComposition> T.show(route: DialogRoute): T {
    return createSingleCommandComposition(Show(route))
}

/**
 * Закрытие диалога
 */
fun <T : NavCommandsComposition> NavigationEventBuilder<T>.dismiss(
        route: DialogRoute
): NavigationEventBuilder<T> = add(Dismiss(route))

/** см [NavigationEventBuilder.dismiss] */
fun <T : NavCommandsComposition> T.dismiss(route: DialogRoute): T {
    return createSingleCommandComposition(Dismiss(route))
}

/**
 * Создает событие навигации с одной навигационной командой
 */
private fun <T : NavCommandsComposition> T.createSingleCommandComposition(command: NavigationCommand): T {
    return this.apply {
        event = NavCommandsEvent(listOf(command))
    }
}
