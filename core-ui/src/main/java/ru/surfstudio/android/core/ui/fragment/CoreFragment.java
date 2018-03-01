package ru.surfstudio.android.core.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope;
import ru.surfstudio.android.logger.Logger;

/**
 * базовая активити для всего приложения
 * см {@link FragmentDelegate}
 */
public abstract class CoreFragment extends Fragment implements CoreFragmentInterface {

    private FragmentDelegate fragmentDelegate;

    @Override
    public FragmentDelegate createFragmentDelegate() {
        return ScreenDelegateFactoryContainer.get().createFragmentDelegate(this);
    }

    @Override
    public BaseFragmentConfigurator createConfigurator() {
        //используется базовый конфигуратор
        return new BaseFragmentConfigurator();
    }

    @Override
    public FragmentPersistentScope getPersistentScope() {
        return fragmentDelegate.getPersistentScope();
    }

    @Override
    public String getName() {
        //уникальное имя по умолчанию для фрагмента контейнера
        return this.getClass().getCanonicalName() + getTag() + getId();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("11111 onCreate" + this.hashCode());
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fragmentDelegate = createFragmentDelegate();
        super.onActivityCreated(savedInstanceState);
        Logger.d("11111 " + this.hashCode() + " onActivityCreated ");
        fragmentDelegate.onCreate(savedInstanceState, null);
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
        Logger.d("1111 onDestroyView" + this.hashCode());
        fragmentDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("11111 onDestroy on " + this.hashCode());
        try {
            fragmentDelegate.onDestroy();
        } catch (NullPointerException e) {
            Logger.e(e.toString() + " 11111 on " + this.hashCode());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.d("11111 onSaveInstance on " + this.hashCode());
        try {
            fragmentDelegate.onOnSaveInstantState(outState);
        } catch (NullPointerException e) {
            Logger.e(e.toString() + " 11111 on " + this.hashCode());
        }
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
