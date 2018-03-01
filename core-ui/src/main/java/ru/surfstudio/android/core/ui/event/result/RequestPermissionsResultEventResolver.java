package ru.surfstudio.android.core.ui.event.result;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.SingleScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class RequestPermissionsResultEventResolver
        extends SingleScreenEventResolver<RequestPermissionsResultEvent, RequestPermissionsResultDelegate> {
    @Override
    public Class<RequestPermissionsResultDelegate> getDelegateType() {
        return RequestPermissionsResultDelegate.class;
    }

    @Override
    public Class<RequestPermissionsResultEvent> getEventType() {
        return RequestPermissionsResultEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected boolean resolve(RequestPermissionsResultDelegate delegate, RequestPermissionsResultEvent event) {
        return delegate.onRequestPermissionsResult(event.getRequestCode(), event.getPermissions(), event.getGrantResults());
    }
}
