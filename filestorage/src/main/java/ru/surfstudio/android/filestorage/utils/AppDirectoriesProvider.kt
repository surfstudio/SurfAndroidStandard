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
package ru.surfstudio.android.filestorage.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import ru.surfstudio.android.utilktx.util.java.CollectionUtils
import java.util.*

object AppDirectoriesProvider {

    fun provideNoBackupStorageDir(context: Context): String {
        return ContextCompat.getNoBackupFilesDir(context)!!.absolutePath
    }

    fun provideBackupStorageDir(context: Context): String {
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null)
        // могут возвращаться null элементы, убираем их
        val filtered = CollectionUtils.filter(Arrays.asList(*externalFilesDirs)) { file -> file != null }
        // берем последний из списка
        val result = CollectionUtils.last(filtered)
        // если подходящего элемента не оказалось, берем директорию внутреннего кэша
        return if (result != null) result.absolutePath else provideNoBackupStorageDir(context)
    }

    //todo SD card priority
    fun provideCacheDir(context: Context): String {
        val externalFilesDirs = ContextCompat.getExternalCacheDirs(context)
        // могут возвращаться null элементы, убираем их
        val filtered = CollectionUtils.filter(Arrays.asList(*externalFilesDirs)) { file -> file != null }
        // берем последний из списка
        val result = CollectionUtils.last(filtered)
        // если подходящего элемента не оказалось, берем директорию внутреннего кэша
        return if (result != null) result.absolutePath else provideNoBackupStorageDir(context)
    }
}