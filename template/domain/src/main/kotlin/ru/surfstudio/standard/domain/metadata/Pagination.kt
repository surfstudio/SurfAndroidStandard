package ru.surfstudio.standard.domain.metadata

/**
 * Модель пагинируемых запросов.
 *
 * @param totalCount общее количество элементов
 * @param pageCount общее количество страниц
 * @param currentPage текущая страница
 * @param perPage размер страницы
 */
data class Pagination(
		val totalCount: Int = 0,
		val pageCount: Int = 0,
		val currentPage: Int = 0,
		val perPage: Int = 0
)