/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.app.migration;

import android.content.SharedPreferences;

import ru.surfstudio.android.shared.pref.SettingsUtil;

import static ru.surfstudio.android.shared.pref.SettingsUtil.EMPTY_INT_SETTING;
import static ru.surfstudio.android.shared.pref.SettingsUtil.EMPTY_LONG_SETTING;


/**
 * хранилище конфигурации запуска устройства
 */
public class AppLaunchConfigurationStorage {

    private static final String LAST_LAUNCH_VERSION = "LAST_LAUNCH_VERSION";
    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";
    private static final String LAST_LAUNCH_VERSION_LONG = "LAST_LAUNCH_VERSION_LONG";

    private SharedPreferences noBackupSharedPref;

    public AppLaunchConfigurationStorage(SharedPreferences noBackupSharedPref) {
        this.noBackupSharedPref = noBackupSharedPref;
    }

    /**
     * Метод, возвращающий последнюю запущенную текущую версию приложения
     *
     * @return - последняя версия
     */
    public long getLastLaunchVersion() {
        int lastVersionInt = SettingsUtil.INSTANCE.getInt(noBackupSharedPref, LAST_LAUNCH_VERSION, EMPTY_INT_SETTING);
        long lastVersionLong = SettingsUtil.INSTANCE.getLong(noBackupSharedPref, LAST_LAUNCH_VERSION_LONG, EMPTY_LONG_SETTING);
        return lastVersionInt == EMPTY_INT_SETTING ? lastVersionLong : lastVersionInt;
    }


    /**
     * Метод, устанавливающий последнюю запущенную текущую версию приложения
     *
     * @param version - последняя версия
     */
    @Deprecated
    public void setLaunchVersion(int version) {
        SettingsUtil.INSTANCE.putInt(noBackupSharedPref, LAST_LAUNCH_VERSION, version, true);
    }

    /**
     * Метод, устанавливающий последнюю запущенную текущую версию приложения
     *
     * @param version - последняя версия
     */
    public void setLaunchVersion(long version) {
        SettingsUtil.INSTANCE.putLong(noBackupSharedPref, LAST_LAUNCH_VERSION_LONG, version, true);
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
        SettingsUtil.INSTANCE.putBoolean(noBackupSharedPref, IS_FIRST_LAUNCH, false, true);
    }

}
