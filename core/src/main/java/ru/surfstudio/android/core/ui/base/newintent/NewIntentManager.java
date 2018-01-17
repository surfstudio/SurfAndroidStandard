package ru.surfstudio.android.core.ui.base.newintent;


import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.event.delegate.NewIntentDelegate;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * позволяет регистрировать обработчики на событие onNewIntent
 * todo слить с ActivityNavigator
 */
@PerScreen
public class NewIntentManager implements NewIntentDelegate {

    @Inject
    public NewIntentManager() {
        //empty
    }

    private Map<NewIntentRoute, Subject> newIntentSubjects = new HashMap<>();

    @Override
    public boolean onNewIntent(Intent intent) {
        for (NewIntentRoute route : newIntentSubjects.keySet()) {
            if(route.parseIntent(intent)) {
                Subject resultSubject = newIntentSubjects.get(route);
                resultSubject.onNext(route);
                return true;
            }
        }
        return false;
    }

    public <T extends NewIntentRoute> Observable<T> observe(Class<T> newIntentRouteClass) {
        try {
            return this.observe(newIntentRouteClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("route class " + newIntentRouteClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }

    public <T extends NewIntentRoute> Observable<T> observe(T newIntentRoute) {
        tryRemoveDuplicateEventSubjects(newIntentRoute);
        PublishSubject<T> eventSubject = PublishSubject.create();
        newIntentSubjects.put(newIntentRoute, eventSubject);
        return eventSubject;
    }

    private void tryRemoveDuplicateEventSubjects(NewIntentRoute eventParser) {
        for (NewIntentRoute registeredRoute : newIntentSubjects.keySet()) {
            if (registeredRoute.getClass().getCanonicalName().equals(eventParser.getClass().getCanonicalName())) {
                newIntentSubjects.get(registeredRoute).onCompleted();
                newIntentSubjects.remove(registeredRoute);
                Log.v(this.getClass().getName(), "duplicate registered NewIntentRoute :"
                        + registeredRoute + " old route unregistered");
            }
        }
    }
}
