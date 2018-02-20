package ru.surfstudio.android.mvp.widget.delegate.factory;

import android.view.View;

import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;


/**
 * Фабрика делегатов виджетов, создана для того чтобы была возможность применить некоторую
 * логику ко всем виджетам конкретного приложения
 */

public interface MvpWidgetDelegateFactory {
    <W extends View & CoreWidgetViewInterface> WidgetViewDelegate createWidgetViewDelegate(W widget);
}
