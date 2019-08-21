/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.navigation.fragment;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Observable;
import ru.surfstudio.android.core.ui.FragmentContainer;
import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.result.BaseActivityResultDelegate;
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute;
import ru.surfstudio.android.core.ui.navigation.Navigator;
import ru.surfstudio.android.core.ui.navigation.ScreenResult;
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute;
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithResultRoute;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;

import static android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;
import static android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
import static android.app.FragmentTransaction.TRANSIT_NONE;

/**
 * позволяет осуществлять навигацияю между фрагментами
 */
public class FragmentNavigator extends BaseActivityResultDelegate implements Navigator {
    protected final ActivityProvider activityProvider;

    @IntDef({TRANSIT_NONE, TRANSIT_FRAGMENT_OPEN, TRANSIT_FRAGMENT_CLOSE, TRANSIT_FRAGMENT_FADE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Transit {
    }

    public FragmentNavigator(ActivityProvider activityProvider,
                             ScreenEventDelegateManager screenEventDelegateManager) {
        screenEventDelegateManager.registerDelegate(this);
        this.activityProvider = activityProvider;
    }

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param routeClass класс маршрута экрана, который должен вернуть результат
     * @param <T>        тип возвращаемых данных
     */
    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
        Class<? extends SupportOnActivityResultRoute<T>> routeClass
    ) {
        try {
            return this.observeOnActivityResult(routeClass.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("route class " + routeClass.getCanonicalName()
                + "must have default constructor", e);
        }
    }

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param route маршрут экрана, который должен вернуть результат
     * @param <T>   тип возвращаемых данных
     */
    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
        SupportOnActivityResultRoute route
    ) {
        return super.observeOnActivityResult(route);
    }

    public void add(FragmentRoute route, boolean stackable, @Transit int transition) {
        start(route, stackable, transition, StartType.ADD);
    }

    public void replace(FragmentRoute route, boolean stackable, @Transit int transition) {
        start(route, stackable, transition, StartType.REPLACE);
    }

    public <T extends Serializable> boolean addFragmentForResult(
        FragmentRoute currentRoute,
        FragmentWithResultRoute<T> nextRoute,
        boolean stackable,
        @Transit int transition
    ) {
        return startFragmentForResult(currentRoute, nextRoute, stackable, transition, StartType.ADD);
    }

    public <T extends Serializable> boolean replaceFragmentForResult(
        FragmentRoute currentRoute,
        FragmentWithResultRoute<T> nextRoute,
        boolean stackable,
        @Transit int transition
    ) {
        return startFragmentForResult(currentRoute, nextRoute, stackable, transition, StartType.REPLACE);
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
     * Закрываает текущий фрагмент c результатом
     *
     * @param currentRoute маршрут текущего экрана
     * @param success           показывает успешное ли завершение
     * @param <T>               тип возвращаемого значения
     */
    public <T extends Serializable> void finishWithResult(
        FragmentWithResultRoute<T> currentRoute,
        boolean success
    ) {
        finishWithResult(currentRoute, null, success);
    }

    /**
     * Закрываает текущий фрагмент c результатом
     *
     * @param currentRoute маршрут текущего экрана
     * @param result            возвращаемый результат
     * @param <T>               тип возвращаемого значения
     */
    public <T extends Serializable> void finishWithResult(
        FragmentWithResultRoute<T> currentRoute,
        T result
    ) {
        finishWithResult(currentRoute, result, true);
    }

    /**
     * Закрываает текущий фрагмент c результатом
     *
     * @param currentRoute маршрут текущего экрана
     * @param result       возвращаемый результат
     * @param success      показывает успешное ли завершение
     * @param <T>          тип возвращаемого значения
     */
    public <T extends Serializable> boolean finishWithResult(
        FragmentWithResultRoute<T> currentRoute,
        T result,
        boolean success
    ) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        Fragment fragment = fragmentManager.findFragmentByTag(currentRoute.getTag());
        if (fragment == null) {
            return false;
        }

        Fragment target = fragment.getTargetFragment();
        if (target == null) {
            return false;
        }
        Intent resultIntent = currentRoute.prepareResultIntent(result);

        target.onActivityResult(
            currentRoute.getRequestCode(),
            success ? Activity.RESULT_OK : Activity.RESULT_CANCELED,
            resultIntent
        );

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

    protected FragmentManager getFragmentManager() {
        return activityProvider.get().getSupportFragmentManager();
    }

    @IdRes
    protected int getViewContainerIdOrThrow() {
        Object contentContainerView = activityProvider.get();
        if (contentContainerView instanceof FragmentContainer) {
            int viewContainerId = ((FragmentContainer) contentContainerView).getContentContainerViewId();
            if (viewContainerId > 0) {
                return viewContainerId;
            }
        }

        throw new IllegalStateException("Container has to have a ContentViewContainer " +
            "implementation in order to make fragment navigation");
    }

    private void start(
        FragmentRoute route,
        boolean stackable,
        @Transit int transition,
        StartType startType
    ) {
        int viewContainerId = getViewContainerIdOrThrow();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (startType) {
            case ADD:
                fragmentTransaction.add(viewContainerId, route.createFragment(), route.getTag());
                break;
            case REPLACE:
                fragmentTransaction.replace(viewContainerId, route.createFragment(), route.getTag());
                break;
        }
        fragmentTransaction.setTransition(transition);
        if (stackable) {
            fragmentTransaction.addToBackStack(route.getTag());
        }

        fragmentTransaction.commit();
    }

    private <T extends Serializable> boolean startFragmentForResult(
        FragmentRoute currentRoute,
        FragmentWithResultRoute<T> nextRoute,
        boolean stackable,
        @Transit int transition,
        StartType startType
    ) {
        if (!super.isObserved(nextRoute)) {
            throw new IllegalStateException("route class " + nextRoute.getClass().getSimpleName()
                + " must be registered by method FragmentNavigator#observeResult");
        }
        int viewContainerId = getViewContainerIdOrThrow();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.executePendingTransactions();

        Fragment currentFragment = fragmentManager.findFragmentByTag(currentRoute.getTag());
        if (currentFragment == null) {
            return false;
        }
        Fragment nextFragment = nextRoute.createFragment();
        nextFragment.setTargetFragment(currentFragment, nextRoute.getRequestCode());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (startType) {
            case ADD:
                fragmentTransaction.add(viewContainerId, nextFragment, nextRoute.getTag());
                break;
            case REPLACE:
                fragmentTransaction.replace(viewContainerId, nextFragment, nextRoute.getTag());
                break;
        }
        fragmentTransaction.setTransition(transition);
        if (stackable) {
            fragmentTransaction.addToBackStack(nextRoute.getTag());
        }

        fragmentTransaction.commit();

        return true;
    }

    private enum StartType {
        ADD, REPLACE
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
