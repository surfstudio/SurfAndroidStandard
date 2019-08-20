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

import androidx.annotation.Nullable;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.UnhandledEventsStore;

/**
 * базовый класс менеджера {@link ScreenEventDelegateManager}
 * занимается регистрацией делегатов и оповещением их о событиях экрана
 */
public class BaseScreenEventDelegateManager implements ScreenEventDelegateManager {

    //список всех поддерживаемых событий и их делегатов
    private List<ScreenEventResolver> eventResolvers;
    //родительский менеджер делегатов
    private ScreenEventDelegateManager parentDelegateManger;
    //тип экрана контейнера
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
        //находим все EventResolvers, посколку delegate может реализовывать сразу несколько интерфейсов делегатов
        List<ScreenEventResolver> supportedResolvers = getEventResolversForDelegate(delegate);
        if (supportedResolvers.isEmpty()) {
            throw new IllegalArgumentException(String.format("No EventResolver for this delegate %s",
                    delegate.getClass().getCanonicalName()));
        }

        registerDelegate(delegate, emitterType, supportedResolvers);
    }

    @Override
    public void registerDelegate(ScreenEventDelegate delegate,
                                 @Nullable ScreenType emitterType,
                                 Class<? extends ScreenEvent> eventType) {
        //Находим резольверы для конкретного события
        registerDelegate(delegate, emitterType, getEventResolversForEvent(eventType));
    }

    private void registerDelegate(ScreenEventDelegate delegate,
                                  @Nullable ScreenType emitterType,
                                  List<ScreenEventResolver> supportedResolvers) {
        assertNotDestroyed();

        for (ScreenEventResolver eventResolver : supportedResolvers) {
            if (eventResolver.getEventEmitterScreenTypes().contains(screenType)
                    && (emitterType == null || screenType == emitterType)) {
                addDelegateToMap(delegatesMap, delegate, eventResolver.getEventType());
            } else {
                if (parentDelegateManger == null) {
                    throw new IllegalStateException(String.format("No BaseScreenEventDelegateManager for addDelegateToMap delegate %s",
                            delegate.getClass().getCanonicalName()));
                }
                addDelegateToMap(throughDelegatesMap, delegate, eventResolver.getEventType());
                parentDelegateManger.registerDelegate(delegate, emitterType, eventResolver.getEventType()); //на конкретное событие
            }
        }
    }

    private List<ScreenEventResolver> getEventResolversForEvent(Class<? extends ScreenEvent> eventType) {
        return Stream.of(eventResolvers)
                .filter((resolvers) -> resolvers.getEventType().equals(eventType))
                .toList();
    }

    private <E extends ScreenEvent> void addDelegateToMap(Map<Class<? extends ScreenEvent>, List<ScreenEventDelegate>> delegatesMap,
                                                          ScreenEventDelegate delegate,
                                                          Class<E> event) {
        List<ScreenEventDelegate> delegates = delegatesMap.get(event);

        if (delegates != null) {
            delegates.add(delegate);
        } else {
            List<ScreenEventDelegate> set = new ArrayList<>();
            set.add(delegate);
            delegatesMap.put(event, set);
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
        List<D> delegates = (List<D>) delegatesMap.get(event.getClass());
        if (delegates == null) {
            delegates = (List<D>) Collections.EMPTY_LIST;
        }
        return eventResolver.resolve(delegates, event);
    }

    @Override
    public <E extends ScreenEvent> boolean unregisterDelegate(ScreenEventDelegate delegate,
                                                              Class<E> eventType) {
        boolean removedFromCurrent = removeDelegate(delegate, eventType, delegatesMap);
        boolean removedFromThrough = removeDelegate(delegate, eventType, throughDelegatesMap);

        boolean removedFromParent = removedFromThrough
                && parentDelegateManger != null
                && parentDelegateManger.unregisterDelegate(delegate, eventType);
        return removedFromCurrent || removedFromParent;
    }

    @Override
    public boolean unregisterDelegate(ScreenEventDelegate delegate) {
        List<Boolean> isRemovedList = new ArrayList<>();

        for (Class<? extends ScreenEvent> eventType : throughDelegatesMap.keySet()) {
            isRemovedList.add(unregisterDelegate(delegate, eventType));
        }

        return Stream.of(isRemovedList).anyMatch((isRemoved) -> isRemoved);
    }

    @Override
    public void destroy() {
        destroyed = true;
        for (Class<? extends ScreenEvent> eventType : throughDelegatesMap.keySet()) {
            for (ScreenEventDelegate delegate : throughDelegatesMap.get(eventType)) {
                if (parentDelegateManger != null) {
                    parentDelegateManger.unregisterDelegate(delegate, eventType);
                }
            }
        }
        throughDelegatesMap.clear();
        delegatesMap.clear();
    }

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

    private <E extends ScreenEvent> boolean removeDelegate(ScreenEventDelegate delegate,
                                                           Class<E> event,
                                                           Map<Class<? extends ScreenEvent>, List<ScreenEventDelegate>> delegatesMap) {
        boolean removed = false;
        List delegateList = delegatesMap.get(event);
        if (delegateList != null) {
            removed = delegateList.remove(delegate);
        }
        return removed;
    }

    private void assertNotDestroyed() {
        if (destroyed) {
            throw new IllegalStateException(String.format("Unsupported operation, EventDelegateManager %s is destroyed", this));
        }
    }

    @Override
    public void sendUnhandledEvents() {
        List<UnhandledEventsStore> resolvers = getStorableResolvers();
        for (UnhandledEventsStore resolver: resolvers) {
            for (ScreenEvent ev : resolver.getUnhandledEvents()) {
                sendEvent(ev);
            }
        }
    }

    private List<UnhandledEventsStore> getStorableResolvers() {
        return Stream.of(eventResolvers)
                .filter((resolver) -> resolver instanceof UnhandledEventsStore)
                .map((screenEventResolver -> (UnhandledEventsStore) screenEventResolver))
                .toList();
    }
}