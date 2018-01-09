package ru.surfstudio.android.core.ui.base.screen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.base.delegate.SupportScreenEventDelegation;
import ru.surfstudio.android.core.ui.base.delegate.manager.BaseScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.delegate.manager.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.delegate.manager.ScreenEventDelegateManager;

/**
 * базовый фрагмент для всего приложения
 * поддерживает механизм делегирования обработки событий экрана {@link ScreenEventDelegate}
 */
public abstract class BaseFragment extends Fragment implements SupportScreenEventDelegation {

    private BaseScreenEventDelegateManager eventDelegateManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventDelegateManager = new FragmentScreenEventDelegateManager((SupportScreenEventDelegation) getActivity());

    }

    @Override
    public ScreenEventDelegateManager getScreenEventDelegateManager() {
        return eventDelegateManager;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eventDelegateManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        eventDelegateManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
