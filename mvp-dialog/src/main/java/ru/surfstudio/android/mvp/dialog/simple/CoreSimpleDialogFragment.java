package ru.surfstudio.android.mvp.dialog.simple;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;


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

    private SimpleDialogDelegate delegate;


    public <A extends ActivityViewPersistentScope> void show(A parentActivityViewPersistentScope) {
        delegate.show(parentActivityViewPersistentScope);
    }

    public <F extends FragmentViewPersistentScope> void show(F parentFragmentViewPersistentScope) {
        delegate.show(parentFragmentViewPersistentScope);
    }

    public <W extends WidgetViewPersistentScope> void show(W parentWidgetViewPersistentScope) {
        delegate.show(parentWidgetViewPersistentScope);
    }

    public <T> T getScreenComponent(Class<T> componentClass) {
        return delegate.getScreenComponent(componentClass);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = new SimpleDialogDelegate(this);
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
