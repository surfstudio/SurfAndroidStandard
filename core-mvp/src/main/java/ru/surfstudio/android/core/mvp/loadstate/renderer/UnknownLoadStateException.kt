package ru.surfstudio.android.core.mvp.loadstate.renderer

/**
 * Ошибка, которая может быть вызвана передачей для рендера неизвестного LoadState в {@link BaseLoadStateRenderer} или в его потомков
 */
class UnknownLoadStateException(stateName: String)
    : Throwable("Unknown LoadState $stateName, add strategy for this LoadState in your LoadStateRenderer")