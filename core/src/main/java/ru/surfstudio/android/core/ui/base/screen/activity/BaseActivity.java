package ru.surfstudio.android.core.ui.base.screen.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.base.delegate.SupportScreenEventDelegation;
import ru.surfstudio.android.core.ui.base.delegate.manager.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.delegate.manager.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.delegate.BaseActivityDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentView;
import ru.surfstudio.android.core.ui.base.screen.view.ContentContainerView;

/**
 * базовая активити для всего приложения
 * поддерживает механизм делегирования обработки событий экрана {@link ScreenEventDelegate}
 * Также имеет компонент @PerActivity scope
 * PersistentScreenScope хранит как компонент Activity (уровня PerActivity) так и компонент экрана
 * (уровня PerScreen), если на основе этой активити создана View
 */
public abstract class BaseActivity extends AppCompatActivity implements SupportScreenEventDelegation, BaseActivityInterface {

    private ActivityScreenEventDelegateManager eventDelegateManager = new ActivityScreenEventDelegateManager();
    private BaseActivityDelegate baseActivityDelegate;

    @Override
    public ScreenEventDelegateManager getScreenEventDelegateManager() {
        return eventDelegateManager;
    }

    @Override
    public BaseActivityDelegate getBaseActivityDelegate() {
        return baseActivityDelegate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivityDelegate = new BaseActivityDelegate(this, this);
        baseActivityDelegate.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eventDelegateManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        eventDelegateManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        eventDelegateManager.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseActivityDelegate.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (this instanceof ContentContainerView) {
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
}
