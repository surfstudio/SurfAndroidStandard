package ${package}.i_${interactorModuleNameWithoutPrefix}

import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

// todo: не забудьте прописать подключение нового модуля в settings.gradle и base_feature.build.gradle

/**
 * Interactor TODO
 */
@PerApplication
class ${interactorModuleNameWithoutPrefix?cap_first}Interactor @Inject constructor()