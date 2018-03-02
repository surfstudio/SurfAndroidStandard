package ru.surfstudio.android.core.ui.scope;


/**
 * контейнер для {@link PersistentScopeStorage}
 */
public class PersistentScopeStorageContainer {

    private static PersistentScopeStorage persistentScopeStorage;

    public static PersistentScopeStorage getPersistentScopeStorage() {
        if (persistentScopeStorage == null) {
            persistentScopeStorage = new PersistentScopeStorage();
        }
        return persistentScopeStorage;
    }

    public static void setPersistentScopeStorage(PersistentScopeStorage customPersistentScopeStorage) {
        persistentScopeStorage = customPersistentScopeStorage;
    }
}
