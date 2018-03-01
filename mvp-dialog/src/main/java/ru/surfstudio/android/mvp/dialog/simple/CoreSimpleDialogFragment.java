package ru.surfstudio.android.mvp.dialog.simple;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;


/**
 * Базовый класс простого диалога который может возвращать результат
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
public abstract class CoreSimpleDialogFragment extends DialogFragment implements CoreSimpleDialogInterface {

    private SimpleDialogDelegate delegate = new SimpleDialogDelegate(this);


    public <A extends FragmentActivity & CoreActivityViewInterface> void show(A parentActivityView) {
        delegate.show(parentActivityView);
    }

    public <F extends Fragment & CoreFragmentViewInterface> void show(F parentFragmentView) {
        delegate.show(parentFragmentView);
    }

    public <W extends View & CoreWidgetViewInterface> void show(W parentWidgetView) {
        delegate.show(parentWidgetView);
    }

    public <T> T getScreenComponent(Class<T> componentClass) {
        return delegate.getScreenComponent(componentClass);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        delegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        delegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        delegate.onPause();
    }
}
