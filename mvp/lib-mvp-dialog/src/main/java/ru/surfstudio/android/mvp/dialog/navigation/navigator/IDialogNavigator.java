package ru.surfstudio.android.mvp.dialog.navigation.navigator;

import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute;

/**
 * Интерфейс для DialogNavigator. Добавлен для обратной совместимости со старым кодом
 */
public interface IDialogNavigator {

    void show(DialogRoute dialogRoute);

    void dismiss(DialogRoute dialogRoute);
}
