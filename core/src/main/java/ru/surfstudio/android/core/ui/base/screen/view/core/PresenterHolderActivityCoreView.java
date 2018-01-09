package ru.surfstudio.android.core.ui.base.screen.view.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;

/**
 * инрефейс для вью, которая оповещает презентер о событиях жизненного цикла экрана
 */
public interface PresenterHolderActivityCoreView extends PresenterHolderCoreView {

    ScreenConfigurator createScreenConfigurator(Activity activity, Intent intent);

    /**
     * @return activity intent
     */
    Intent getStartIntent();

    /**
     * @return layout resource of the screen
     */
    @LayoutRes
    int getContentView();

    void setContentView(int contentView);

    /**
     * @param viewRecreated render whether view created in first time or recreated after
     *                      changing configuration
     */
    void onCreate(Bundle savedInstanceState, @Nullable PersistableBundle persistentState, boolean viewRecreated);

    /**
     * Called before Presenter is bound to the View and content view is created
     *
     * @param savedInstanceState
     * @param viewRecreated
     */
    void onPreCreate(Bundle savedInstanceState, @Nullable PersistableBundle persistentState, boolean viewRecreated);

}
