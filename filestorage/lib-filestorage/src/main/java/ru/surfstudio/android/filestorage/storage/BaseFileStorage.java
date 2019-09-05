/*
  Copyright (c) 2018-present, SurfStudio LLC. Vasily Beglyanin, Margarita Volodina.

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
package ru.surfstudio.android.filestorage.storage;

import androidx.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.surfstudio.android.filestorage.converter.ObjectConverter;
import ru.surfstudio.android.filestorage.encryptor.EmptyEncryptor;
import ru.surfstudio.android.filestorage.encryptor.Encryptor;
import ru.surfstudio.android.filestorage.naming.NamingProcessor;
import ru.surfstudio.android.filestorage.processor.FileProcessor;

/**
 * Класс, позволяющий хранить обьекты в кеше, является оберткой над {@link FileProcessor}
 *
 * @param <T>
 */
public abstract class BaseFileStorage<T> {

    private final FileProcessor fileProcessor;
    private final NamingProcessor namingProcessor;
    private final ObjectConverter<T> objectConverter;

    private Encryptor encryptor;

    public BaseFileStorage(FileProcessor fileProcessor,
                           NamingProcessor namingProcessor,
                           ObjectConverter<T> objectConverter) {
        this.fileProcessor = fileProcessor;
        this.namingProcessor = namingProcessor;
        this.objectConverter = objectConverter;
        this.encryptor = new EmptyEncryptor();
    }

    public BaseFileStorage(FileProcessor fileProcessor,
                           NamingProcessor namingProcessor,
                           ObjectConverter<T> objectConverter,
                           Encryptor encryptor) {
        this.fileProcessor = fileProcessor;
        this.namingProcessor = namingProcessor;
        this.objectConverter = objectConverter;
        this.encryptor = encryptor;
    }

    private void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    /**
     * Метод, который очищает все элементы кэша в конкретном кэш класс-типе
     */
    public synchronized void clear() {
        fileProcessor.deleteAll();
    }

    public synchronized boolean contains(@NotNull String key) {
        final String name = convertName(key);
        return fileProcessor.containsFile(name);
    }

    /**
     * Метод, который возвращает значение по определенному ключу или null, если не существует
     *
     * @param key - ключ (не может быть null)
     * @return - данные или null - если не существует
     */
    @Nullable
    public T get(@NotNull String key) {
        final String name = convertName(key);
        return getInternal(name);
    }

    /**
     * Метод, который возвращает значение по определенному ключу или null, если не существует
     *
     * @param key       - ключ (не может быть null)
     * @param encryptor - encryptor для дешифрования данных
     * @return - данные или null - если не существует
     */
    @Nullable
    public T get(@NotNull String key, Encryptor encryptor) {
        setEncryptor(encryptor);
        return get(key);
    }

    /**
     * Метод, который кодирует объект в массив байтов и сохраняет его в файловой системе или перезаписывает текущий файл.
     *
     * @param key - ключ (не может быть null)
     * @param t   - значение (не может быть null)
     */
    public void put(@NotNull String key, @NotNull T t) {
        final String name = convertName(key);
        putInternal(t, name);
    }

    /**
     * Метод, который кодирует объект в массив байтов и сохраняет его в файловой системе или перезаписывает текущий файл.
     *
     * @param key       - ключ (не может быть null)
     * @param t         - значение (не может быть null)
     * @param encryptor - encryptor для шифрования данных
     */
    public void put(@NotNull String key, @NotNull T t, Encryptor encryptor) {
        setEncryptor(encryptor);
        put(key, t);
    }

    /**
     * Метод, который удаляет значение с помощью определенного ключа или не делает ничего, если объекта по ключу не существует
     *
     * @param key - ключ (не может быть null)
     */
    public synchronized void remove(@NotNull String key) {
        final String name = convertName(key);
        fileProcessor.remove(name);
    }

    /**
     * Метод, возвращающий все элементы кэша по класс-типу
     *
     * @return - все элементы, содержащиеся в директиве класс-типа
     */
    public synchronized List<T> getAll() {
        return Stream.of(fileProcessor.getNames())
                .map(this::getInternal)
                .collect(Collectors.toList());
    }

    private synchronized T getInternal(final String name) {
        byte[] bytes = fileProcessor.getBytesOrNull(name);
        if (bytes == null) {
            return null;
        }
        return objectConverter.decode(encryptor.decrypt(bytes));
    }

    private synchronized void putInternal(@NotNull final T t, final String name) {
        byte[] encode = encryptor.encrypt(objectConverter.encode(t));
        fileProcessor.saveBytesOrRewrite(name, encode);
    }

    private String convertName(final String key) {
        return namingProcessor.getNameFrom(key);
    }
}
