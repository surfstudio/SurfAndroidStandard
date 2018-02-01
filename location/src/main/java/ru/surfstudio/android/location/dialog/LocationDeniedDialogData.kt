package ru.surfstudio.android.location.dialog

import java.io.Serializable


/**
 * Данные для заполнения UI контента отображения диалогого окна
 * если нет разрешения на геолокационный сервис у приложения
 */
class LocationDeniedDialogData(val title: String?,
                               val message: String,
                               val cancelBtn: String,
                               val goToSettingsBtn: String?) : Serializable