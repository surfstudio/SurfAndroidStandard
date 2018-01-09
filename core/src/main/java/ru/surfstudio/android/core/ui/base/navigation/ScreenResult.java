package ru.surfstudio.android.core.ui.base.navigation;

/**
 * результат выполнения экрана
 * @param <T>
 */
public class ScreenResult<T> {
    private boolean success;
    private T data;

    public ScreenResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public boolean hasData(){
        return data != null;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }
}
