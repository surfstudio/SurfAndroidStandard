package ru.surfstudio.android.mvp.dialog.simple;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

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

    <A extends FragmentActivity & CoreActivityViewInterface> void show(A parentActivityView);

    <F extends Fragment & CoreFragmentViewInterface> void show(F parentFragment);

    <W extends View & CoreWidgetViewInterface> void show(W parentWidgetView);

    <T> T getScreenComponent(Class<T> componentClass);
}
