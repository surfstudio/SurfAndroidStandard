package ru.surfstudio.android.core.ui.base.navigation.fragment;


import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.navigation.Navigator;
import ru.surfstudio.android.core.ui.base.navigation.fragment.route.FragmentRoute;
import ru.surfstudio.android.core.ui.base.screen.view.ContentContainerView;

import static android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;
import static android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
import static android.app.FragmentTransaction.TRANSIT_NONE;

/**
 * позволяет осуществлять навигацияю между фрагментами
 */
public class FragmentNavigator implements Navigator {
    protected final ActivityProvider activityProvider;

    @IntDef({TRANSIT_NONE, TRANSIT_FRAGMENT_OPEN, TRANSIT_FRAGMENT_CLOSE, TRANSIT_FRAGMENT_FADE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Transit {
    }

    public FragmentNavigator(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    public void add(FragmentRoute route, boolean stackable, @Transit int transition) {
        int viewContainerId = getViewContainerIdOrThrow();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(viewContainerId, route.createFragment(), route.getTag());
        fragmentTransaction.setTransition(transition);
        if (stackable) {
            fragmentTransaction.addToBackStack(route.getTag());
        }

        fragmentTransaction.commit();
    }

    public void replace(FragmentRoute route, boolean stackable, @Transit int transition) {
        int viewContainerId = getViewContainerIdOrThrow();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewContainerId, route.createFragment(), route.getTag());
        fragmentTransaction.setTransition(transition);
        if (stackable) {
            fragmentTransaction.addToBackStack(route.getTag());
        }

        fragmentTransaction.commit();
    }

    /**
     * @return возвращает {@code true} если фрагмент был удален успешно
     */
    public boolean remove(FragmentRoute route, @Transit int transition) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        Fragment fragment = fragmentManager.findFragmentByTag(route.getTag());
        if (fragment == null) {
            return false;
        }

        fragmentManager.beginTransaction()
                .setTransition(transition)
                .remove(fragment)
                .commit();

        return true;
    }

    /**
     * @return возвращает {@code true} если фрагмент успешно отобразился
     */
    public boolean show(FragmentRoute route, @Transit int transition) {
        return toggleVisibility(route, true, transition);
    }

    /**
     * @return возвращает {@code true} если фрагмент был скрыт успешно
     */
    public boolean hide(FragmentRoute route, @Transit int transition) {
        return toggleVisibility(route, false, transition);
    }

    /**
     * @return возвращает {@code true} если какой-либо фрагмент верхнего уровня был удален из стека
     */
    public boolean popBackStack() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        return fragmentManager.popBackStackImmediate();
    }

    /**
     * Очищает стек фрагментов до роута.
     * Пример:
     * Фрагменты А, Б, С, Д добавлены в стек
     * popBackStack(Б, true) очистит стек до Б включительно
     * то есть, останется в стеке только А
     * или
     * popBackStack(Б, false) в стеке останется А и Б,
     * Б не удаляется из стека
     *
     * @param route     очистка до уровня роута
     * @param inclusive удалить стек включая и роут
     * @return возвращает {@code true} если фрагмент(ы) был(и) удален(ы) из стека
     */
    public boolean popBackStack(@NonNull FragmentRoute route, boolean inclusive) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        boolean routeFoundInBackStack = false;
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry backStack = fragmentManager.getBackStackEntryAt(i);
            if (route.getTag().equals(backStack.getName())) {
                routeFoundInBackStack = true;
                break;
            }
        }

        if (!routeFoundInBackStack) {
            return false;
        }

        Fragment fragment = fragmentManager.findFragmentByTag(route.getTag());
        if (fragment == null) {
            return false;
        }

        for (int i = fragmentManager.getBackStackEntryCount() - 1; i >= 0; i--) {
            FragmentManager.BackStackEntry backStack = fragmentManager.getBackStackEntryAt(i);
            Fragment backStackFragment = fragmentManager.findFragmentByTag(backStack.getName());
            if (backStackFragment == fragment) {
                break;
            }
        }

        return fragmentManager.popBackStackImmediate(route.getTag(),
                inclusive ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
    }

    /**
     * Очистка бэкстека
     *
     * @return true если успешно
     */
    public boolean clearBackStack() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        final int backStackCount = fragmentManager.getBackStackEntryCount();
        if (backStackCount == 0) {
            return false;
        }

        for (int i = 0; i < backStackCount; i++) {
            FragmentManager.BackStackEntry backStack = fragmentManager.getBackStackEntryAt(i);
            Fragment fragment = fragmentManager.findFragmentByTag(backStack.getName());
        }

        return fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(backStackCount - 1).getName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Вызывает принудительно onResume
     *
     * @return true если успешно
     * */
    public boolean onResume(FragmentRoute route) {
        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(route.getTag());
        if (fragment != null) {
            fragment.onResume();
        } else {
            return false;
        }

        return true;
    }

    protected FragmentManager getFragmentManager() {
        return activityProvider.get().getSupportFragmentManager();
    }

    @IdRes
    protected int getViewContainerIdOrThrow() {
        Object contentContainerView = activityProvider.get();
        if (contentContainerView instanceof ContentContainerView) {
            int viewContainerId = ((ContentContainerView) contentContainerView).getContentContainerViewId();
            if (viewContainerId > 0) {
                return viewContainerId;
            }
        }

        throw new IllegalStateException("Container has to have a ContentViewContainer " +
                "implementation in order to make fragment navigation");
    }

    private boolean toggleVisibility(FragmentRoute route, boolean show, @Transit int transition) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        Fragment fragment = fragmentManager.findFragmentByTag(route.getTag());
        if (fragment == null) {
            return false;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(transition);
        if (show) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.hide(fragment);
        }

        fragmentTransaction.commit();
        return true;
    }
}
