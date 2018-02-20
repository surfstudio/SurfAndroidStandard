package ru.surfstudio.android.core.ui.scope;

/**
 * Интерфейс для всех экранов, имеющих свой {@link PersistentScope}
 */

public interface HasPersistentScope {
    PersistentScope getPersistentScope();
}
