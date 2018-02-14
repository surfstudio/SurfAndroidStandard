package ru.surfstudio.android.core.ui.base.screen.configurator;


import ru.surfstudio.android.core.ui.base.screen.dialog.simple.CoreSimpleDialogInterface;

/**
 * интерфейс для конфигураторов вью, предоставляет компонент экрана,
 * который используется для {@link CoreSimpleDialogInterface} //todo
 */
public interface ViewConfigurator extends Configurator {

    ScreenComponent getScreenComponent();
}
