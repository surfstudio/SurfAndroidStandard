package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormEvent.*
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * StateHolder экрана [InputFormActivityView]
 */
@PerScreen
class InputFormStateHolder @Inject constructor() {
    val inputString = State(EMPTY_STRING)
}

/**
 * Реактор экрана [InputFormActivityView]
 */
@PerScreen
class InputFormReactor @Inject constructor() : Reactor<InputFormEvent, InputFormStateHolder> {

    override fun react(sh: InputFormStateHolder, event: InputFormEvent) {
        when (event) {
            is InputChanged -> sh.inputString.accept(event.input)
        }
    }
}