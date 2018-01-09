package ru.surfstudio.android.core.app.log;

import android.support.annotation.NonNull;

import timber.log.Timber;

/**
 * логгирует в logcat
 * все логи начиная с уровня DEBUG логгируются в {@link RemoteLogger}
 */
public class Logger {

    private Logger() {
        //do nothing
    }

    public static void init(){
        Timber.plant(new LoggerTree());
    }

    /** Log a verbose developerMessage with optional format args. */
    public static void v(@NonNull String message, Object... args) {
        setClickableLink();
        Timber.v(message, args);
    }

    /** Log a verbose exception and a developerMessage with optional format args. */
    public static void v(Throwable t, @NonNull String message, Object... args) {
        setClickableLink();
        Timber.v(t, message, args);
    }

    /** Log a debug developerMessage with optional format args. */
    public static void d(@NonNull String message, Object... args) {
        setClickableLink();
        Timber.d(message, args);
    }

    /** Log a debug exception and a developerMessage with optional format args. */
    public static void d(Throwable t, @NonNull String message, Object... args) {
        setClickableLink();
        Timber.d(t, message, args);
    }

    /** Log an info developerMessage with optional format args. */
    public static void i(@NonNull String message, Object... args) {
        setClickableLink();
        Timber.i(message, args);
    }

    /** Log an info exception and a developerMessage with optional format args. */
    public static void i(Throwable t, @NonNull String message, Object... args) {
        setClickableLink();
        Timber.i(t, message, args);
    }

    /**
     * используется для ожидаемых ошибок
     * Логгирует только сообщение ошибки
     * @param e
     */
    public static void w(Throwable e){
        i(String.format("Error: %s", e.getMessage()));
    }

    /** Log a warning developerMessage with optional format args. */
    public static void w(@NonNull String message, Object... args) {
        setClickableLink();
        Timber.w(message, args);
    }

    /** Log a warning exception and a developerMessage with optional format args. */
    public static void w(Throwable t, @NonNull String message, Object... args) {
        setClickableLink();
        Timber.w(t, message, args);
    }

    /** Log an error developerMessage with optional format args. */
    public static void e(@NonNull String message, Object... args) {
        setClickableLink();
        Timber.e(message, args);
    }

    /** Log an error exception and a developerMessage with optional format args. */
    public static void e(Throwable t, @NonNull String message, Object... args) {
        setClickableLink();
        Timber.e(t, message, args);
    }

    public static void e(Throwable t){
        setClickableLink();
        Timber.e(t, "Error");
    }

    /**
     * Установка кликабельной метки, которая переводит к местонахождению лога
     * (ClassName.java:35)
     */
    private static void setClickableLink() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length < 2) {
            throw new IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        StackTraceElement element = stackTrace[2];
        String tagMsg = String.format("(%s:%s) ", element.getFileName(), element.getLineNumber());
        Timber.tag(tagMsg);
    }
}