package ru.surfstudio.android.core.ui.event.lifecycle.ready;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.event.base.ScreenEvent;

/**
 * событие готовности иерархии вью
 * выбросится когда вью будет проинициализирована полностью,
 * например после вызова метода {@link CoreActivityInterface#onCreate(Bundle, PersistableBundle, boolean)}
 */
public class OnViewReadyEvent implements ScreenEvent {
}
