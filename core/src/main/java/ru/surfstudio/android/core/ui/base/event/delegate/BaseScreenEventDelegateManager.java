package ru.surfstudio.android.core.ui.base.event.delegate;

import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

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
    private Set<ScreenEventDelegate> throughDelegates = new HashSet<>();
    private List<ScreenEventResolver> eventResolvers;
    private ScreenEventDelegateManager parentDelegateManger;
    private ScreenType screenType;

    public BaseScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers,
                                          @Nullable ScreenEventDelegateManager parentDelegateManger,
                                          ScreenType screenType) {
        this.eventResolvers = eventResolvers;
        this.parentDelegateManger = parentDelegateManger;
        this.screenType = screenType;
    }

    @Override
    public void registerDelegate(ScreenEventDelegate delegate) {
        ScreenEventResolver eventResolver = getEventResolverForDelegate(delegate);
        if (eventResolver == null) {
            throw new IllegalArgumentException(String.format("No EventResolver for this delegate %s",
                    delegate.getClass().getCanonicalName()));
        }
        if (eventResolver.getEventEmitterScreenTypes().contains(screenType)) {
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


    @Override
    public <E extends ScreenEvent, D extends ScreenEventDelegate, R> R sendEvent(E event) {
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

    private <D extends ScreenEventDelegate> List<D> getDelegates(Class<? extends D> clazz) {
        return Stream.of(delegates)
                .filter(clazz::isInstance)
                .map(delegate -> (D) delegate)
                .collect(Collectors.toList());
    }

    @Nullable
    private ScreenEventResolver getEventResolverForDelegate(ScreenEventDelegate delegate) {
        for (ScreenEventResolver eventResolver : eventResolvers) {
            if (eventResolver.getDelegateType().isInstance(delegate)) {
                return eventResolver;
            }
        }
        return null;
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
}