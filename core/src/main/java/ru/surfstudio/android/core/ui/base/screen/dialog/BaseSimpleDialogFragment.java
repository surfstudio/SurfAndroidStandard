package ru.surfstudio.android.core.ui.base.screen.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenComponent;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.RemoteLogger;

/**
 * Базовый класс простого диалога который может возвращать результат
 * У этого диалога презентер не предусмотрен
 * Простой диалог рассматривается как часть родителького View и оповещает презентер о событиях
 * пользователя прямым вызовом метода презентера
 *
 * для получения презентера в дмалоге предусмотрен метод {@link #getScreenComponent(Class)} который
 * возвращает компонент родительского экрана.
 *
 * Этот диалог следует расширять если не требуется реализация сложной логики в диалоге и обращение
 * к слою Interactor
 */
public abstract class BaseSimpleDialogFragment extends BaseDialogFragment implements HasName {
    public static final String EXTRA_PARENT = "EXTRA_PARENT"; //todo parent widget
    private Parent parentType;

    public <A extends FragmentActivity & CoreActivityViewInterface> void show(A parentActivityView) {
        parentType = Parent.ACTIVITY;
        show(parentActivityView.getSupportFragmentManager());
    }

    public <F extends Fragment & CoreFragmentViewInterface> void show(F parentFragment) {
        parentType = Parent.FRAGMENT;
        this.setTargetFragment(parentFragment, 0);
        show(parentFragment.getFragmentManager());
    }

    protected void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, getName());
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        throw new UnsupportedOperationException("Instead this method, use method render(parentFragment) " +
                "or render(parentActivity)");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        throw new UnsupportedOperationException("Instead this method, use method render(parentFragment) " +
                "or render(parentActivity)");
    }

    protected <T> T getScreenComponent(Class<T> componentClass) {
        ScreenComponent screenComponent;
        switch (parentType) {
            case ACTIVITY:
                screenComponent = ((CoreActivityViewInterface) getActivity()).getConfigurator().getScreenComponent();
                break;
            case FRAGMENT:
                screenComponent = ((CoreFragmentViewInterface) getTargetFragment()).getConfigurator().getScreenComponent();
                break;
            default:
                throw new IllegalStateException("Unsupported parent type: " + parentType);
        }
        return componentClass.cast(screenComponent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (parentType == null) {
            parentType = (Parent)savedInstanceState.getSerializable(EXTRA_PARENT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_PARENT, parentType);
    }

    @Override
    public void onResume() {
        super.onResume();
        RemoteLogger.logMessage(String.format(LogConstants.LOG_DIALOG_RESUME_FORMAT, getName()));
    }

    @Override
    public void onPause() {
        super.onPause();
        RemoteLogger.logMessage(String.format(LogConstants.LOG_DIALOG_PAUSE_FORMAT, getName()));
    }

    private enum Parent {
        ACTIVITY,
        FRAGMENT
    }

}
