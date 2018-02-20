package ru.surfstudio.android.mvp.widget.delegate.factory;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.List;

import ru.surfstudio.android.core.ui.event.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorageContainer;
import ru.surfstudio.android.mvp.widget.delegate.ParentPersistentScopeFinder;
import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;


/**
 * Фабрика делегатов виджетов по умолчанию, предоставляет стандартные делегаты
 */
public class DefaultMvpWidgetDelegateFactory implements MvpWidgetDelegateFactory {

    @Override
    public <W extends View & CoreWidgetViewInterface> WidgetViewDelegate createWidgetViewDelegate(W widget) {
        PersistentScopeStorage scopeStorage = getScopeStorage((FragmentActivity) widget.getContext());
        return new WidgetViewDelegate(
                widget,
                scopeStorage,
                new ParentPersistentScopeFinder(widget, scopeStorage));
    }

    @NonNull
    protected List<ScreenEventResolver> getEventResolvers() {
        return ScreenEventResolverHelper.standardEventResolvers();
    }

    @NonNull
    protected PersistentScopeStorage getScopeStorage(FragmentActivity activity) {
        return PersistentScopeStorageContainer.getFrom(activity);
    }
}
