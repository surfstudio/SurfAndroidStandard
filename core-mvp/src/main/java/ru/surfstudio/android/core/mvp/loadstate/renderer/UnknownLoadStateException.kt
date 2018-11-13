package ru.surfstudio.android.core.mvp.loadstate.renderer

/**
 * Exception, which can be thrown in case of unknown LoadState in {@link BaseLoadStateRenderer} and it's descendants
 */
class UnknownLoadStateException(stateName: String)
    : Throwable("Unknown LoadState $stateName, add strategy for thi s LoadState in your LoadStateRenderer")