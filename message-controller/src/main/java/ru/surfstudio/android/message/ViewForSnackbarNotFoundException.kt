package ru.surfstudio.android.message

/**
 * Икслючение, возникающее, если для показа snackbar не была найдена view
 */
class ViewForSnackbarNotFoundException(developerMessage: String) : RuntimeException(developerMessage)