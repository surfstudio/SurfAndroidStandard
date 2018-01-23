package ru.surfstudio.android.core.ui.base.screen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.screen.delegate.BaseFragmentDelegate;

/**
 * базовый фрагмент для всего приложения
 * поддерживает механизм делегирования обработки событий экрана {@link ScreenEventDelegate}
 */
public abstract class BaseFragment extends Fragment implements BaseFragmentInterface {

    private BaseFragmentDelegate baseFragmentDelegate;

    @Override
    public BaseFragmentDelegate createFragmentDelegate() {
        return new BaseFragmentDelegate(this);
    }

    @Override
    public String getName() {
        return baseFragmentDelegate.getName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baseFragmentDelegate = createFragmentDelegate();
        baseFragmentDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        baseFragmentDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        baseFragmentDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        baseFragmentDelegate.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        baseFragmentDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baseFragmentDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseFragmentDelegate.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        baseFragmentDelegate.onOnSaveInstantState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        baseFragmentDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        baseFragmentDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
