package ru.surfstudio.android.core.ui.event.lifecycle.ready;


import android.os.Bundle;
import android.os.PersistableBundle;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие готовности иерархии вью
 * вызовется когда вью будет проинициализирована полностью,
 * например после вызова метода {@link CoreActivityInterface#onCreate(Bundle, PersistableBundle, boolean)}
 */
public interface OnViewReadyDelegate extends ScreenEventDelegate {

    void onViewReady();
}
