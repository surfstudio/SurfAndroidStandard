package ru.surfstudio.android.core.ui.base.screen.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import ru.surfstudio.android.core.ui.base.screen.delegate.BaseActivityDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentView;
import ru.surfstudio.android.core.ui.base.screen.view.ContentContainerView;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * базовая активити для всего приложения
 * поддерживает механизм делегирования обработки событий экрана {@link ScreenEventDelegate}
 * Также имеет компонент @PerActivity scope
 * PersistentScope хранит как компонент Activity (уровня PerActivity) так и компонент экрана
 * (уровня PerScreen), если на основе этой активити создана View
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseActivityInterface {

    private BaseActivityDelegate baseActivityDelegate;

    @Override
    public BaseActivityDelegate getBaseActivityDelegate() {
        return baseActivityDelegate;
    }

    @Override
    public BaseActivityDelegate createBaseActivityDelegate() {
        return new BaseActivityDelegate(this);
    }

    @Override
    public String getName() {
        return baseActivityDelegate.getName();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivityDelegate = createBaseActivityDelegate();
        baseActivityDelegate.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        baseActivityDelegate.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseActivityDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baseActivityDelegate.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        baseActivityDelegate.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseActivityDelegate.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        baseActivityDelegate.onOnSaveInstantState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        baseActivityDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        baseActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        baseActivityDelegate.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if(baseActivityDelegate.onBackPressed()){
            return;
        }
        if (this instanceof ContentContainerView) { //todo
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            for (int i = fragmentList.size() - 1; i >= 0; i--) {
                Fragment fragment = fragmentList.get(i);
                if (fragment.isVisible() && fragment instanceof CoreFragmentView
                        && ((CoreFragmentView) fragment).onBackPressed()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) { //todo
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
