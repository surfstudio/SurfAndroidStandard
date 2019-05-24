package ru.surfstudio.android.build

fun getModulesName(): List<Pair<String, String>> {
    val components = parseComponentJson()
    val names = ArrayList<Pair<String, String>>()
    components.forEach { component ->
        component.libs.forEach { lib ->
            names.add(lib.dir to "${component.dir}/${lib.dir}")
        }
        component.samples.forEach { sample ->
            names.add(sample to "${component.dir}/$sample")
        }
    }
    return names
}