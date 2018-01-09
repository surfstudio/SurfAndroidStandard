package ru.surfstudio.android.core.ui.base.screen.delegate;

import android.support.v4.app.FragmentActivity;

import com.agna.ferro.core.PSSActivityDelegate;
import com.agna.ferro.core.PSSDelegate;

import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;

/**
 * делегат для любой активити, создает и управляет @PerActivity scope

 */
public class BaseActivityDelegate {

    private BaseActivityInterface baseActivity;
    private FragmentActivity fragmentActivity;
    private PSSDelegate pssDelegate;
    private BaseActivityConfigurator screenConfigurator;

    public BaseActivityDelegate(FragmentActivity fragmentActivity, BaseActivityInterface baseActivity) {
        this.fragmentActivity = fragmentActivity;
        this.baseActivity = baseActivity;
    }

    public void onCreate() {
        pssDelegate = createPssDelegate();
        pssDelegate.init();
        screenConfigurator = baseActivity.createActivityConfigurator();
        screenConfigurator.setPersistentScreenScope(pssDelegate.getScreenScope());
        screenConfigurator.init();
    }

    private PSSDelegate createPssDelegate() {
        return new PSSActivityDelegate(baseActivity, fragmentActivity);
    }

    public void onDestroy() {
        pssDelegate.onDestroy();
    }

    public PSSDelegate getPssDelegate() {
        return pssDelegate;
    }

    public BaseActivityConfigurator getScreenConfigurator() {
        return screenConfigurator;
    }
}
