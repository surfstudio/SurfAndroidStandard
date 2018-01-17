package ru.surfstudio.android.core.ui.base.scope;


import android.support.v4.app.FragmentActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.scope.activity.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.scope.fragment.FragmentPersistentScope;

//todo package local constructor
public class PersistentScopeManager {
    private final List<ScreenEventResolver> eventResolvers;
    private final Map<String, PersistentScope> scopes = new HashMap<>();

    public PersistentScopeManager(List<ScreenEventResolver> eventResolvers) {
        this.eventResolvers = eventResolvers;
    }

    public static PersistentScopeManager get(FragmentActivity activity) {
        return PersistentScopeManagerContainer.getOrCreate(activity).getPersistentScopeManager();
    }

    public PersistentScope getScope(String name){
        return scopes.get(name);
    }

    public void onFinallyDestroy() {
        for(PersistentScope scope : scopes.values()){
            scope.getScreenEventDelegateManager().sendEvent()
        }
    }

    public PersistentScope createActivityScope(String scopeName){
        checkNotExist(scopeName);
        for(PersistentScope scope : scopes.values()){
            if(scope.getScreenType() == ScreenType.ACTIVITY){
                throw new IllegalStateException("PersistentScopeManager can contains only one ActivityPersistentScope");
            }
        }
        ActivityPersistentScope activityScope = new ActivityPersistentScope(scopeName, new ActivityScreenEventDelegateManager(eventResolvers));
        scopes.put(scopeName, activityScope);
        return activityScope;
    }

    public PersistentScope createFragmentScope(String scopeName){
        checkNotExist(scopeName);
        ActivityPersistentScope activityPersistentScope = null;
        for(PersistentScope scope : scopes.values()){
            if(scope.getScreenType() == ScreenType.ACTIVITY){
                activityPersistentScope = (ActivityPersistentScope)scope;
            }
        }
        if(activityPersistentScope == null){
            throw new IllegalStateException("FragmentPersistentScope cannot be created without ActivityPersistentScope");
        }
        FragmentScreenEventDelegateManager eventDelegateManager = new FragmentScreenEventDelegateManager(
                eventResolvers,
                activityPersistentScope.getScreenEventDelegateManager());
        FragmentPersistentScope fragmentScope = new FragmentPersistentScope(scopeName, eventDelegateManager);
        scopes.put(scopeName, fragmentScope);
        return fragmentScope;
    }

    private void checkNotExist(String scopeName) {
        if(getScope(scopeName)!=null){
            throw new IllegalStateException(String.format("Scope with name %s already created", scopeName));
        }
    }
}
