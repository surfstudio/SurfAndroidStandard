package ru.surfstudio.android.location.exceptions

/**
 * Исключение, возникающее, если все поставщики местоположения выключены (GPS provider, network provider).
 */
class LocationProvidersAreDisabledException : RuntimeException()