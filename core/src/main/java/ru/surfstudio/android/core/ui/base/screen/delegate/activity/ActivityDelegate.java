package ru.surfstudio.android.core.ui.base.screen.delegate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.base.BaseScreenDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.event.back.OnBackPressedEvent;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.newintent.NewIntentEvent;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;

/**
 * делегат для базовой активити,
 * управляет ключевыми сущностями внутренней логики экрана:
 * - PersistentScope            - хранилище для всех остальных обьектов, переживает смену конфигурации
 * - ScreenEventDelegateManager - позволяет подписываться на события экрана
 * - ScreenState                - хранит текущее состояние экрана
 * - ScreenConfigurator         - управляет компонентами даггера, предоставляет уникальное имя экрана
 */
public class ActivityDelegate extends BaseScreenDelegate<
        ActivityPersistentScope,
        ActivityScreenState,
        BaseActivityConfigurator,
        ActivityCompletelyDestroyChecker> {

    private FragmentActivity activity;
    private CoreActivityInterface coreActivity;
    private final PersistentScopeStorage scopeStorage;

    public <A extends FragmentActivity & CoreActivityInterface> ActivityDelegate(
            A activity,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            ActivityCompletelyDestroyChecker completelyDestroyChecker) {
        super(scopeStorage, eventResolvers, completelyDestroyChecker);
        this.activity = activity;
        this.coreActivity = activity;
        this.scopeStorage = scopeStorage;
    }

    @Override
    protected void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState) {
        getScreenState().onCreate(activity, savedInstanceState);
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle) {
        coreActivity.onCreate(savedInstanceState, persistableBundle, getScreenState().isViewRecreated());
    }

    @Override
    protected BaseActivityConfigurator createConfigurator() {
        return coreActivity.createConfigurator();
    }

    @NonNull
    @Override
    protected ActivityPersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers) {
        ActivityScreenEventDelegateManager eventDelegateManager =
                new ActivityScreenEventDelegateManager(eventResolvers);
        ActivityScreenState screenState = new ActivityScreenState();
        return new ActivityPersistentScope(
                getName(),
                eventDelegateManager,
                screenState);
    }

    @Override
    protected ActivityPersistentScope getPersistentScope() {
        return scopeStorage.getActivityScope();
    }

    //activity specific events

    public void onNewIntent(Intent intent) {
        getEventDelegateManager().sendEvent(new NewIntentEvent(intent));
    }

    public boolean onBackPressed() {
        return getEventDelegateManager().sendEvent(new OnBackPressedEvent());
    }
}
