package ru.surfstudio.android.core.ui.base.screen.dialog;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;

import javax.inject.Inject;

import ru.surfstudio.android.core.ui.base.error.ErrorHandler;
import ru.surfstudio.android.core.ui.base.event.delegate.SupportScreenEventDelegation;
import ru.surfstudio.android.core.ui.base.event.delegate.manager.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.manager.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.configurator.ViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.MvpViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.view.HandleableErrorView;

/**
 * Базовый класс диалога с презентером
 * <p>
 * Этот диалог рассматривается как самостаятельный экран
 * Этот диалог следует расширять когда требуется реализовать сложную логику или обращаться к
 * слою Interactor
 * <p>
 * Для возвращения результата следует использовать RxBus
 */
public abstract class BaseComplexBottomSheetDialogFragmentView extends BottomSheetDialogFragment implements
        CoreFragmentViewInterface, SupportScreenEventDelegation, HandleableErrorView {

    @Inject
    ErrorHandler standardErrorHandler;

    private ActivityScreenEventDelegateManager delegateManager = new ActivityScreenEventDelegateManager();

    private MvpViewDelegate viewDelegate;

    @Override
    public ScreenEventDelegateManager getScreenEventDelegateManager() {
        return delegateManager;
    }

    @Override
    public void handleError(Throwable error) {
        getErrorHandler().handleError(error);
    }

    public PersistentScope getScreenScope() {
        return viewDelegate.getScreenScope();
    }

    protected ErrorHandler getErrorHandler() {
        return standardErrorHandler;
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

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewDelegate = new FragmentViewDelegate(getActivity(), this, this);
        viewDelegate.onPreMvpViewCreate();
        viewDelegate.onMvpViewCreate(savedInstanceState, null);
    }

    @Override
    public ViewConfigurator getScreenConfigurator() {
        return viewDelegate.getScreenConfigurator();
    }

    @Override
    public String getName() {
        return getScreenConfigurator().getName();
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
    public Bundle getStartArgs() {
        return getArguments();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewDelegate.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroy();
    }
}
