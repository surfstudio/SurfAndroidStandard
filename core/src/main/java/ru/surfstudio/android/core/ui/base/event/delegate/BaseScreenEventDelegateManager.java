package ru.surfstudio.android.core.ui.base.event.delegate;

import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;

/**
 * базовый класс менеджера {@link ScreenEventDelegate}
 * занимается регистрацией оповещением делегатов о событиях экрана
 * todo onDestroy()
 */
public class BaseScreenEventDelegateManager implements ScreenEventDelegateManager {

    private Set<ScreenEventDelegate> delegates = new HashSet<>();
    private Set<ScreenEventDelegate> throughDelegates = new HashSet<>(); //todo
    private List<ScreenEventResolver> eventResolvers;
    private ScreenEventDelegateManager parentDelegateManger;
    private ScreenType screenType;
    private boolean destroyed = false;

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
        List<ScreenEventResolver> supportedResolvers = getEventResolversForDelegate(delegate);
        if (supportedResolvers.isEmpty()) {
            throw new IllegalArgumentException(String.format("No EventResolver for this delegate %s",
                    delegate.getClass().getCanonicalName()));
        }
        for (ScreenEventResolver eventResolver : supportedResolvers) {
            if (eventResolver.getEventEmitterScreenTypes().contains(screenType)
                    && (emitterType == null || screenType != emitterType)) { //todo check logic
                delegates.add(delegate);
            } else {
                if (parentDelegateManger == null) {
                    throw new IllegalStateException(String.format("No BaseScreenEventDelegateManager for register delegate %s",
                            delegate.getClass().getCanonicalName()));
                }
                throughDelegates.add(delegate);
                parentDelegateManger.registerDelegate(delegate);
            }
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
        Class<D> delegateType = eventResolver.getDelegateType();
        List<D> delegates = getDelegates(delegateType);
        return eventResolver.resolve(delegates, event);
    }

    @Override
    public boolean unregisterDelegate(ScreenEventDelegate delegate) {
        boolean removedFromCurrent = delegates.remove(delegate);
        boolean removedFromParent = parentDelegateManger != null
                && parentDelegateManger.unregisterDelegate(delegate);
        return removedFromCurrent || removedFromParent;
    }

    @Override
    public void destroy() {
        destroyed = true;
        for (ScreenEventDelegate delegate : throughDelegates) {
            if (parentDelegateManger != null) {
                parentDelegateManger.unregisterDelegate(delegate);
            }
        }
        throughDelegates.clear();
    }

    private <D extends ScreenEventDelegate> List<D> getDelegates(Class<? extends D> clazz) {
        return Stream.of(delegates)
                .filter(clazz::isInstance)
                .map(delegate -> (D) delegate)
                .collect(Collectors.toList());
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

    private void assertNotDestroyed() {
        if (destroyed) {
            throw new IllegalStateException(String.format("Unsupported operation, EventDelegateManager %s is destroyed", this));
        }
    }
}