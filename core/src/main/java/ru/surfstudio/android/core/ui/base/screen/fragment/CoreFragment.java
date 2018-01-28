package ru.surfstudio.android.core.ui.base.screen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.CoreApp;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentDelegate;

/**
 * базовый фрагмент для всего приложения
 * поддерживает механизм делегирования обработки событий экрана {@link ScreenEventDelegate}
 */
public abstract class CoreFragment extends Fragment implements CoreFragmentInterface {

    private FragmentDelegate fragmentDelegate;

    @Override
    public FragmentDelegate createFragmentDelegate() {
        return CoreApp.getScreenDelegateFactory(getContext()).createFragmentDelegate(this);
    }

    @Override
    public BaseFragmentConfigurator createConfigurator() {
        //используется базовый конфигуратор, предоставляющий только имя экрана
        return new BaseFragmentConfigurator(this);
    }

    @Override
    public BaseFragmentConfigurator getConfigurator() {
        return fragmentDelegate.getConfigurator();
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
