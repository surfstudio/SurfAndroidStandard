package ru.surfstudio.android.mvp.widget.event.delegate;


import java.util.List;

import androidx.annotation.Nullable;
import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.BaseScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

public class WidgetScreenEventDelegateManager extends BaseScreenEventDelegateManager {

    public WidgetScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers,
                                            @Nullable ScreenEventDelegateManager parentDelegateManger) {
        super(eventResolvers, parentDelegateManger, ScreenType.WIDGET);
    }
}
