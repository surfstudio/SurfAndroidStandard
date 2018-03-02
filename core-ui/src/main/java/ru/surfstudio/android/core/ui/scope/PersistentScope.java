package ru.surfstudio.android.core.ui.scope;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Хранилище основных обьектов экрана, необходимых для внутренней логики ядра
 * Также хранит соответствующие даггер компоненты
 * Для каждого активити, фрагмента и WidgetView создается инстанс PersistentScope
 * Не уничтожается при смене конфигурации
 * Для Доступа к этому обьекту следует использовать {@link PersistentScopeStorage}
 */
public class PersistentScope {
    protected final Map<ObjectKey, Object> objects = new HashMap<>();

    private final String scopeId = UUID.randomUUID().toString();

    public String getScopeId() {
        return scopeId;
    }
}
