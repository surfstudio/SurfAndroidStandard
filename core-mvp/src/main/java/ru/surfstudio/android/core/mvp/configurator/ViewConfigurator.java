package ru.surfstudio.android.core.mvp.configurator;


import ru.surfstudio.android.core.ui.configurator.Configurator;

/**
 * интерфейс для конфигураторов вью, предоставляет компонент экрана,
 * который используется для простых диалогов
 */
public interface ViewConfigurator extends Configurator {

    ScreenComponent getScreenComponent();
}
