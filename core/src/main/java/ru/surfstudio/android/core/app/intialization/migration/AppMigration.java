package ru.surfstudio.android.core.app.intialization.migration;


import ru.surfstudio.android.logger.Logger;

/**
 * Базовый класс миграции с одной версии приложения на другую
 */
public abstract class AppMigration {

    private final int baseVersion;

    public AppMigration(int value) {
        baseVersion = value;
    }

    public void execute(int oldVer, int newVer) {
        if (oldVer < baseVersion && baseVersion <= newVer) {
            Logger.d("Executing app migration, baseVersion = " + baseVersion);
            apply();
        }
    }

    protected abstract void apply();

    public int getBaseVersion() {
        return baseVersion;
    }
}
