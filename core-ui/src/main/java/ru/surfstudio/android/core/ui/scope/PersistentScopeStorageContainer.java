package ru.surfstudio.android.core.ui.scope;


/**
 * контейнер для {@link PersistentScopeStorage}
 *
 * Существует возможность изменить PersistentScopeStorage на кастомный
 * с помощью метода setPersistentScopeStorage()
 */
public class PersistentScopeStorageContainer {

    private static PersistentScopeStorage persistentScopeStorage;

    public static PersistentScopeStorage getPersistentScopeStorage() {
        if (persistentScopeStorage == null) {
            persistentScopeStorage = new PersistentScopeStorage();
        }
        return persistentScopeStorage;
    }

    /**
     * Изменение storage
     * @param customPersistentScopeStorage наследник PersistentScopeStorage, реализующий определенную логику
     */
    public static void setPersistentScopeStorage(PersistentScopeStorage customPersistentScopeStorage) {
        persistentScopeStorage = customPersistentScopeStorage;
    }
}
