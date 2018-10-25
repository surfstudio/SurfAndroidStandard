/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.filestorage

object CacheConstant {

    /**
     * Имя в аннотации [javax.inject.Named] для пути к директории хранилища приложения, не подверженной бэкапу
     */
    const val NO_BACKUP_STORAGE_DIR_NAME = "noBackupStorageDir"

    /**
     * Имя в аннотации [javax.inject.Named] для пути к директории хранилища приложения, подверженной бэкапу
     */
    const val BACKUP_STORAGE_DIR_NAME = "backupStorageDir"

    /**
     * Имя в аннотации [javax.inject.Named] для пути к директории кэша
     */
    const val CACHE_DIR_NAME = "cacheDir"
}