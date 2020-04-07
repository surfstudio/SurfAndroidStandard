package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data

internal data class KittenUi(
        val data: Kitten = Kitten(),
        val fallbackPicturePreviewUrl: String = "https://i.ytimg.com/vi/Ep3jK1bZrB8/maxresdefault.jpg"
) {
    val name: String get() = data.name
    val picturePreviewUrl: String get() = when {
        data.picturePreviewUrl.isNotBlank() -> data.picturePreviewUrl
        else -> fallbackPicturePreviewUrl
    }
}