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


import ru.surfstudio.android.logger.Logger;

/**
 * Базовый класс миграции с одной версии приложения на другую
 */
public abstract class AppMigration {

    private final long baseVersion;

    @Deprecated
    public AppMigration(int value) {
        baseVersion = value;
    }

    public AppMigration(long value) {
        baseVersion = value;
    }

    @Deprecated
    public void execute(int oldVer, int newVer) {
        execute((long) oldVer, (long) newVer);
    }

    public void execute(long oldVer, long newVer) {
        if (oldVer < baseVersion && baseVersion <= newVer) {
            Logger.d("Executing app migration, baseVersion = " + baseVersion);
            apply();
        }
    }

    protected abstract void apply();

    public long getBaseVersion() {
        return baseVersion;
    }
}
