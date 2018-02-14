package ru.surfstudio.android.core.ui.base.screen.widjet;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.widget.WidgetViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.scope.HasPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * Интерфейс для всех кастомных вьюшек с презентером
 *
 * !!!ВАЖНО!!!
 * 1) Необходимо вызвать метод init во время onCreate() Activity или onActivityCreated() Fragment
 * 2) кастомная вьюшка с презентером может быть только в статической иерархии вью,
 *      то есть должна создаваться при старте экрана, и не может быть использована при
 *      динамическом создании вью, в том числе внутри элементов RecyclerView
 *
 * Эти ограничения связаны с большими сложностями реализации делегирования событий экрана
 * для динамических вью
 */

public interface CoreWidgetViewInterface extends
        PresenterHolderCoreView,
        HasConfigurator,
        HasPersistentScope,
        HasName {

    @Override
    BaseWidgetViewConfigurator createConfigurator();

    @Override
    WidgetViewPersistentScope getPersistentScope();

    WidgetViewDelegate createWidgetViewDelegate();

    /**
     * Необходимо вызвать метод init во время Activity.onCreate() или Fragment.onActivityCreated()
     * или CoreWidgetView.onCreate()
     */
    void init();

    void onCreate();
}
