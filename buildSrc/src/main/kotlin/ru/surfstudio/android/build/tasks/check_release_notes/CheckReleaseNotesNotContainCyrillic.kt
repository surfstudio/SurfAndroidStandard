package ru.surfstudio.android.build.tasks.check_release_notes

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.exceptions.release_notes.ReleaseNotesContainsCyrillicException
import ru.surfstudio.android.build.model.release_notes.ReleaseNotesInfo

/**
 * Check ReleaseNotes does not contain cyrillic symbols.
 */
open class CheckReleaseNotesNotContainCyrillic : DefaultTask() {

    @TaskAction
    fun check() {

        Components.value.forEach { component ->
            val releaseNotes: ReleaseNotesInfo = ReleaseNotes.findByComponentName(component.name)

            if (releaseNotes.title.hasCyrillic || releaseNotes.versions.any { version ->
                        version.version.hasCyrillic || version.libraries.any { library ->
                            library.name.hasCyrillic || library.items.any { item ->
                                item.content.hasCyrillic
                            }
                        }
                    }
            ) {
                throw ReleaseNotesContainsCyrillicException(releaseNotes)
            }
        }
    }

    private val String.hasCyrillic: Boolean get() = find {
        val block = Character.UnicodeBlock.of(it)
        block == Character.UnicodeBlock.CYRILLIC
                || block == Character.UnicodeBlock.CYRILLIC_SUPPLEMENTARY
                || block == Character.UnicodeBlock.CYRILLIC_EXTENDED_A
                || block == Character.UnicodeBlock.CYRILLIC_EXTENDED_B
    } != null
}