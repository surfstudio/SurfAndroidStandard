/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.event.base.resolver;

import android.util.Log;

import java.util.List;

import ru.surfstudio.android.core.ui.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * базовый тип ScreenEventResolver, который позволяет отбработать событие только одним делегатом
 * из всех зарегистрированных соответствующего типа
 *
 * @param <E> событие
 * @param <D> делегат
 */
public abstract class SingleScreenEventResolver<E extends ScreenEvent, D extends ScreenEventDelegate>
        implements ScreenEventResolver<E, D, Boolean> {

    protected abstract boolean resolve(D delegate, E event);

    @Override
    public Boolean resolve(List<D> delegates, E event) {
        if (delegates.isEmpty()) {
            return false;
        }
        D someDelegate = delegates.get(0);
        for (D delegate : delegates) {
            if (resolve(delegate, event)) {
                return true;
            }
        }
        logUnhandled(someDelegate.getClass(), event);
        return false;
    }

    private void logUnhandled(Class delegateClass, ScreenEvent event) {
        String tag = this.getClass().getSimpleName();
        Log.i(tag, String.format("Unhandled screen event: %s "
                        + "\nProbably you do not register delegate %s "
                        + "\nor you handle this event on fragment",
                event.toString(), delegateClass.getSimpleName()));
    }
}
