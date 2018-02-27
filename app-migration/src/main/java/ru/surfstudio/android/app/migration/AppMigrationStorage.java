package ru.surfstudio.android.app.migration;

/**
 * Должна существовать имплементация поставляемая инъекцией через модуль.
 */
public interface AppMigrationStorage {
    AppMigration[] getMigrations();
}
