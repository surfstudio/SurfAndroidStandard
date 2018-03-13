package ru.surfstudio.android.app.migration;

import android.content.SharedPreferences;

import ru.surfstudio.android.shared.pref.SettingsUtil;


/**
 * хранилище конфигурации запуска устройства
 */
public class AppLaunchConfigurationStorage {

    private static final String LAST_LAUNCH_VERSION = "LAST_LAUNCH_VERSION";
    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";

    private SharedPreferences noBackupSharedPref;

    public AppLaunchConfigurationStorage(SharedPreferences noBackupSharedPref) {
        this.noBackupSharedPref = noBackupSharedPref;
    }

    /**
     * Метод, возвращающий последнюю запущенную текущую версию приложения
     *
     * @return - последняя версия
     */
    public int getLastLaunchVersion() {
        return SettingsUtil.INSTANCE.getInt(noBackupSharedPref, LAST_LAUNCH_VERSION);
    }


    /**
     * Метод, устанавливающий последнюю запущенную текущую версию приложения
     *
     * @param version - последняя версия
     */
    public void setLaunchVersion(int version) {
        SettingsUtil.INSTANCE.putInt(noBackupSharedPref, LAST_LAUNCH_VERSION, version);
    }

    /**
     * Метод, возвращающий флаг признака первого запуска приложения
     *
     * @return - true - первый запуск, false - последующие запуски
     */
    public boolean isFirstLaunch() {
        return SettingsUtil.INSTANCE.getBoolean(noBackupSharedPref, IS_FIRST_LAUNCH, true);
    }

    /**
     * Метод, устанавливающий флаг признака первого запуска приложения
     * После вызова этого метода, {@link #isFirstLaunch()} будет всегда возвращать {@code true}
     */
    public void markFirstLaunchDone() {
        SettingsUtil.INSTANCE.putBoolean(noBackupSharedPref, IS_FIRST_LAUNCH, false);
    }

}
