/*
  Copyright (c) 2018-present, SurfStudio LLC. Vasily Beglyanin.

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
package ru.surfstudio.android.filestorage.processor;

import androidx.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.surfstudio.android.logger.Logger;

/**
 * Класс для осуществления операций с файлами кэша
 */
public class FileProcessor {

    private final File rootDir;
    private final int maxFilesCount;

    public FileProcessor(final String baseCacheDir, final String cacheDirName, final int maxFilesCount) {
        this.maxFilesCount = maxFilesCount;
        final File cacheDir = new File(baseCacheDir, "file_cache");
        tryMakeDir(cacheDir);

        rootDir = new File(cacheDir, cacheDirName);
        tryMakeDir(rootDir);
    }

    /**
     * Метод, который очищает все файлы определенной папки класса кэш
     */
    public void deleteAll() {
        Stream.of(getFilesList())
                .filter(File::canRead)
                .filter(File::canWrite)
                .forEach(File::delete);
    }

    /**
     * Метод, который проверяет наличие файла с помощью определенного ключа
     *
     * @param key - ключ
     * @return true - если файл существует или false - если файл не существует
     */
    public boolean containsFile(@NotNull String key) {
        return new File(rootDir, key).exists();
    }

    /**
     * Метод, который возвращает массив байтов из файла с помощью определенного ключа или null, если не существует
     *
     * @param key - ключ
     * @return - массив байт если существует, null - если не существует или ошибка ввода - вывода
     */
    @SuppressWarnings("squid:S1168")
    @Nullable
    public byte[] getBytesOrNull(@NotNull String key) {
        try {
            return getBytesOrNullUnsafe(key);
        } catch (IOException e) {
            Logger.e(e);
            return null;
        }
    }

    @SuppressWarnings({"squid:S2095", "squid:S1168"})
    @Nullable
    private byte[] getBytesOrNullUnsafe(@NotNull String key) throws IOException {
        if (containsFile(key)) {
            final File file = new File(rootDir, key);
            ByteBuffer buffer;
            try (
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                    FileChannel fileChannel = randomAccessFile.getChannel()
            ) {
                final long fileSize = fileChannel.size();
                buffer = ByteBuffer.allocate((int) fileSize);
                fileChannel.read(buffer);
                buffer.flip();
                return buffer.array();
            }
        }
        return null;
    }

    /**
     * Метод, возвращающий все имена по текущей директиве класс-типа
     *
     * @return - имена всех файлов по класс-типу
     */
    public List<String> getNames() {
        return Stream.of(getFilesList())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    /**
     * Метод, который сохраняет массив байтов в файловой системе или переписывает текущий файл.
     *
     * @param key    - ключ
     * @param encode - массив байтов
     */
    public void saveBytesOrRewrite(@NotNull String key, @NotNull byte[] encode) {
        try {
            saveBytesOrRewriteUnsafe(key, encode);
            tryRemoveOldFile();
        } catch (IOException e) {
            Logger.e(e);
        }
    }

    /**
     * удаляет старые файлы если превишен максимальный лимит
     */
    private void tryRemoveOldFile() {
        List<File> filesList = getFilesList();
        if (filesList.size() > maxFilesCount) {
            Stream.of(filesList)
                    .sorted((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()))
                    .skip(maxFilesCount)
                    .forEach(File::delete);
        }
    }

    @SuppressWarnings("squid:S2095")
    private void saveBytesOrRewriteUnsafe(@NotNull String key, @NotNull byte[] encode) throws IOException {
        final File file = new File(rootDir, key);
        //noinspection ResultOfMethodCallIgnored
        file.delete();
        try (
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                FileChannel fileChannel = randomAccessFile.getChannel()
        ) {
            final ByteBuffer buffer = ByteBuffer.allocate(encode.length);
            buffer.put(encode);
            buffer.flip();
            fileChannel.write(buffer);
        }
    }

    /**
     * Метод, который удаляет файл с помощью определенного ключа или ничего не делает, если файла по ключу не существует
     *
     * @param key - ключ
     */
    public void remove(String key) {
        Stream.of(getFilesList())
                .filter(it -> it.getName().equals(key))
                .forEach(File::delete);
    }

    private void tryMakeDir(final File dir) {
        //noinspection ResultOfMethodCallIgnored
        dir.mkdir();
    }

    @NotNull
    private List<File> getFilesList() {
        File[] listFiles = rootDir.listFiles();
        return listFiles != null ? Arrays.asList(listFiles) : new ArrayList<>();
    }
}
