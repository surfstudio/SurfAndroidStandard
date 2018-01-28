package ru.surfstudio.android.core.ui.base.screen.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ru.surfstudio.android.core.CoreApp;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentView;
import ru.surfstudio.android.core.ui.base.screen.view.ContentContainerView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * базовая активити для всего приложения
 * поддерживает механизм делегирования обработки событий экрана {@link ScreenEventDelegate}
 * Также имеет компонент @PerActivity scope
 * PersistentScope хранит как компонент Activity (уровня PerActivity) так и компонент экрана
 * (уровня PerScreen), если на основе этой активити создана View
 */
public abstract class CoreActivity extends AppCompatActivity implements CoreActivityInterface {

    private ActivityDelegate activityDelegate;

    @Override
    public ActivityDelegate createActivityDelegate() {
        return CoreApp.getScreenDelegateFactory(this).createActivityDelegate(this);
    }

    @Override
    public BaseActivityConfigurator getConfigurator() {
        return activityDelegate.getConfigurator();
    }

    @Override
    public final String getName() {
        return getConfigurator().getName();
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDelegate = createActivityDelegate();
        onPreCreate(savedInstanceState);
        setContentView(getContentView());
        activityDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, boolean viewRecreated) {
        //empty
    }

    /**
     * @return layout resource of the screen
     */
    @LayoutRes
    abstract protected int getContentView();

    /**
     * Called before Presenter is bound to the View and content view is created
     *
     * @param savedInstanceState
     */
    void onPreCreate(Bundle savedInstanceState) {
        //empty
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityDelegate.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityDelegate.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityDelegate.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDelegate.onDestroyView();
        activityDelegate.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityDelegate.onOnSaveInstantState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        activityDelegate.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (activityDelegate.onBackPressed()) {
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
