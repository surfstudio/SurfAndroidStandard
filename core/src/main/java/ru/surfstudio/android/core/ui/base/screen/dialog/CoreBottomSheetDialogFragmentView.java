package ru.surfstudio.android.core.ui.base.screen.dialog;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;

/**
 * Базовый класс диалога с презентером
 * <p>
 * Этот диалог рассматривается как самостаятельный экран
 * Этот диалог следует расширять когда требуется реализовать сложную логику или обращаться к
 * слою Interactor
 * <p>
 * Для возвращения результата следует использовать RxBus
 */
public abstract class CoreBottomSheetDialogFragmentView extends BottomSheetDialogFragment implements
        CoreFragmentViewInterface {

    private FragmentDelegate fragmentDelegate;

    protected abstract CorePresenter[] getPresenters();

    @Override
    public abstract BaseFragmentViewConfigurator createConfigurator();

    @Override
    public FragmentViewDelegate createFragmentDelegate() {
        return ScreenDelegateFactoryContainer.get().createFragmentViewDelegate(this);
    }

    @Override
    public BaseFragmentViewConfigurator getConfigurator() {
        return (BaseFragmentViewConfigurator) fragmentDelegate.getConfigurator();
    }

    /**
     * Override this instead {@link #onActivityCreated(Bundle)}
     *
     * @param viewRecreated show whether view created in first time or recreated after
     *                      changing configuration
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState, boolean viewRecreated) {

    }

    /**
     * Bind presenter to this view
     * You can override this method for support different presenters for different views
     */
    @Override
    public void bindPresenters() {
        for (CorePresenter presenter : getPresenters()) {
            presenter.attachView(this);
        }
    }

    @Override
    public final String getName() {
        return getConfigurator().getName();
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentDelegate = createFragmentDelegate();
        fragmentDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentDelegate.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDelegate.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentDelegate.onOnSaveInstantState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fragmentDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
