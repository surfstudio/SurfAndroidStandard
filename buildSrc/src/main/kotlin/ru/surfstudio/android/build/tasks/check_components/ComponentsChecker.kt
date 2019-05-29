package ru.surfstudio.android.build.tasks.check_components

import ru.surfstudio.android.build.model.Component

interface ComponentsChecker {
    fun getChangedComponents() : List<Component>
}