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
package ru.surfstudio.android.mvp.dialog.simple;

import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;

/**
 * Интерфейс для диалогов без презентерапростого диалога который может возвращать результат
 * У этого диалога презентер не предусмотрен
 * Простой диалог рассматривается как часть родителького View и оповещает презентер о событиях
 * пользователя прямым вызовом метода презентера
 * <p>
 * для получения презентера в дмалоге предусмотрен метод {@link #getScreenComponent(Class)} который
 * возвращает компонент родительского экрана.
 * <p>
 * Этот диалог следует расширять если не требуется реализация сложной логики в диалоге и обращение
 * к слою Interactor
 */

public interface CoreSimpleDialogInterface extends HasName {

    <A extends ActivityViewPersistentScope> void show(A parentActivityViewPersistentScope, String tag);

    <F extends FragmentViewPersistentScope> void show(F parentFragmentViewPersistentScope, String tag);

    <W extends WidgetViewPersistentScope> void show(W parentWidgetViewPersistentScope, String tag);

    <T> T getScreenComponent(Class<T> componentClass);
}
