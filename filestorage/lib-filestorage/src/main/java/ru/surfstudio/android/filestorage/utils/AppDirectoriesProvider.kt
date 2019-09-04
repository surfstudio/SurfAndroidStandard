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
import androidx.core.content.ContextCompat
import java.io.File

object AppDirectoriesProvider {

    fun provideNoBackupStorageDir(context: Context): String {
        return ContextCompat.getNoBackupFilesDir(context)!!.absolutePath
    }

    fun provideBackupStorageDir(context: Context): String = context.filesDir.absolutePath

    fun provideCacheDir(context: Context): String {
        return provideDir(ContextCompat.getExternalCacheDirs(context), context.cacheDir)
    }
    
    /**
     * Функция, возвращающая корректную директорию
     *
     * @param primaryFileArray массив файлов, последний элемент которого будет возвращен в качестве результата,
     * если массив содержит не null элементы
     * @param secondaryFile файл, директория которого будет возвращена в качестве результата,
     * если в первом массиве подходящего элемента не было найдено
     * @return директория
     */
    private fun provideDir(primaryFileArray: Array<File?>, secondaryFile: File): String {
        return primaryFileArray.last { it != null }?.absolutePath
                ?: secondaryFile.absolutePath
    }
}