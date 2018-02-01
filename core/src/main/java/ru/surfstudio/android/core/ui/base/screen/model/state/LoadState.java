package ru.surfstudio.android.core.ui.base.screen.model.state;


public enum LoadState {
    UNSPECIFIED, //не определен
    NONE, //контент загружен
    MAIN_LOADING, //прогресс, закрывающий весь контент
    TRANSPARENT_LOADING, //полупрозрачный прогресс, блокирует весь интерфейс
    SMALL_LOADING, //индикатор загрузки, не блокирующий интерфейс, может быть ptr
    ERROR, //ошибка загрузки данных
    EMPTY, //данных нет
    NOT_FOUND, //нет данных по заданному фильтру
}
