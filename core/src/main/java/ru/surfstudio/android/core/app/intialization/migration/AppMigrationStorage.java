package ru.surfstudio.android.core.app.intialization.migration;

/**
 * Должна существовать имплементация поставляемая инъекцией через модуль.
 */
public interface AppMigrationStorage {
    AppMigration[] getMigrations();
}
