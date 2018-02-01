package ru.surfstudio.android.core.ui.base.screen.configurator;


import ru.surfstudio.android.core.ui.base.screen.dialog.BaseSimpleDialogFragment;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;

/**
 * интерфейс для конфигураторов вью, предоставляет компонент экрана,
 * который используется для {@link BaseSimpleDialogFragment}
 */
public interface ViewConfigurator<P extends PersistentScope> extends Configurator<P> {

    ScreenComponent getScreenComponent();
}
