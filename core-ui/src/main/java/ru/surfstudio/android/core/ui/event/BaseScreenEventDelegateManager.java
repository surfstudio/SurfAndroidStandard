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
package ru.surfstudio.android.core.ui.event;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

/**
 * базовый класс менеджера {@link ScreenEventDelegateManager}
 * занимается регистрацией делегатов и оповещением их о событиях экрана
 */
public class BaseScreenEventDelegateManager implements ScreenEventDelegateManager {

    //список всех поддерживаемых событий и их делегатов
    private List<ScreenEventResolver> eventResolvers;
    //родительский менеджер делегатов
    private ScreenEventDelegateManager parentDelegateManger;
    //тип эрана контейнера
    private ScreenType screenType;
    private boolean destroyed = false;

    //список делегатов, зарегистрированных в менеджере по эвенту
    private Map<Class<? extends ScreenEvent>, List<ScreenEventDelegate>> delegatesMap = new HashMap<>();
    //список делегатов, зарегистрированных в менеджере родителя по эвенту
    private Map<Class<? extends ScreenEvent>, List<ScreenEventDelegate>> throughDelegatesMap = new HashMap<>();

    public BaseScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers,
                                          @Nullable ScreenEventDelegateManager parentDelegateManger,
                                          ScreenType screenType) {
        this.eventResolvers = eventResolvers;
        this.parentDelegateManger = parentDelegateManger;
        this.screenType = screenType;
    }

    @Override
    public void registerDelegate(ScreenEventDelegate delegate) {
        registerDelegate(delegate, null);
    }

    @Override
    public void registerDelegate(ScreenEventDelegate delegate, @Nullable ScreenType emitterType) {
        assertNotDestroyed();
        //находим все EventResolvers, посколку delegate может реализовывать сразу несколько интерфейсов делегатов
        List<ScreenEventResolver> supportedResolvers = getEventResolversForDelegate(delegate);
        if (supportedResolvers.isEmpty()) {
            throw new IllegalArgumentException(String.format("No EventResolver for this delegate %s",
                    delegate.getClass().getCanonicalName()));
        }
        for (ScreenEventResolver eventResolver : supportedResolvers) {
            if (eventResolver.getEventEmitterScreenTypes().contains(screenType)
                    && (emitterType == null || screenType == emitterType)) {
                register(delegate, eventResolver.getEventType());
            } else {
                if (parentDelegateManger == null) {
                    throw new IllegalStateException(String.format("No BaseScreenEventDelegateManager for register delegate %s",
                            delegate.getClass().getCanonicalName()));
                }
                register(throughDelegatesMap, delegate, eventResolver.getEventType());
                parentDelegateManger.registerDelegate(delegate);
            }
        }
    }

    @Override
    public <E extends ScreenEvent> void register(ScreenEventDelegate delegate, Class<E> event) {
        assertNotDestroyed();

        register(delegatesMap, delegate, event);
    }

    private <E extends ScreenEvent> void register(Map<Class<? extends ScreenEvent>, List<ScreenEventDelegate>> delegatesMap,
                                                  ScreenEventDelegate delegate,
                                                  Class<E> event) {
        List<ScreenEventDelegate> delegates = delegatesMap.get(event);

        if (delegates != null) {
            delegates.add(delegate);
        } else {
            List<ScreenEventDelegate> list = new ArrayList<>();
            list.add(delegate);
            delegatesMap.put(event, list);
        }
    }

    @Override
    public <E extends ScreenEvent, D extends ScreenEventDelegate, R> R sendEvent(E event) {
        assertNotDestroyed();
        ScreenEventResolver<E, D, R> eventResolver = getEventResolverForEvent(event);
        if (eventResolver == null) {
            throw new IllegalArgumentException(String.format("No EventResolver for this event %s",
                    event.getClass().getCanonicalName()));
        }
        if (!eventResolver.getEventEmitterScreenTypes().contains(screenType)) {
            throw new IllegalArgumentException(String.format("event %s cannot be emitted from %s",
                    event.getClass().getCanonicalName(), screenType));
        }
        //Class<D> delegateType = eventResolver.getDelegateType();
        List<D> delegates = (List<D>) delegatesMap.get(event.getClass());
        if (delegates == null) {
            delegates = (List<D>) Collections.EMPTY_LIST;
        }
        return eventResolver.resolve(delegates, event);
    }

    @Override
    public <E extends ScreenEvent> boolean unregisterDelegate(ScreenEventDelegate delegate,
                                                              Class<E> event) {
        boolean removedFromCurrent = false;
        List delegateList = delegatesMap.get(event);
        if (delegateList != null) {
            removedFromCurrent = delegatesMap.get(event).remove(delegate);
        }

        boolean removedFromThrough = false;
        List throughDelegatesList = throughDelegatesMap.get(event);
        if (throughDelegatesList != null) {
            removedFromThrough = throughDelegatesMap.get(event).remove(delegate);
        }

        boolean removedFromParent = removedFromThrough
                && parentDelegateManger != null
                && parentDelegateManger.unregisterDelegate(delegate, event);
        return removedFromCurrent || removedFromParent;
    }

    @Override
    public void destroy() {
        destroyed = true;
        for (Class<? extends ScreenEvent> event : throughDelegatesMap.keySet()) {
            for (ScreenEventDelegate delegate : throughDelegatesMap.get(event)) {
                if (parentDelegateManger != null) {
                    parentDelegateManger.unregisterDelegate(delegate, event);
                }
            }
        }
        throughDelegatesMap.clear();
        delegatesMap.clear();
    }

    /*private <D extends ScreenEventDelegate> List<D> getDelegates(Class<? extends D> clazz) {
        return Stream.of(delegates)
                .filter(clazz::isInstance)
                .map(delegate -> (D) delegate)
                .collect(Collectors.toList());
    }*/

    private List<ScreenEventResolver> getEventResolversForDelegate(ScreenEventDelegate delegate) {
        List<ScreenEventResolver> resultResolvers = new ArrayList<>();
        for (ScreenEventResolver eventResolver : eventResolvers) {
            if (eventResolver.getDelegateType().isInstance(delegate)) {
                resultResolvers.add(eventResolver);
            }
        }
        return resultResolvers;
    }

    @Nullable
    private <E extends ScreenEvent, D extends ScreenEventDelegate, R> ScreenEventResolver<E, D, R> getEventResolverForEvent(E event) {
        for (ScreenEventResolver eventResolver : eventResolvers) {
            if (eventResolver.getEventType().isInstance(event)) {
                return eventResolver;
            }
        }
        return null;
    }

    private void assertNotDestroyed() {
        if (destroyed) {
            throw new IllegalStateException(String.format("Unsupported operation, EventDelegateManager %s is destroyed", this));
        }
    }
}