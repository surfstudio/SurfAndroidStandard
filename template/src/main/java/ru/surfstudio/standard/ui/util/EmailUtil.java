package ru.surfstudio.standard.ui.util;


import ru.surfstudio.standard.interactor.common.error.NonInstanceClassCreateException;

/**
 * Утилиты для работы с email
 */
public class EmailUtil {

    private EmailUtil() {
        throw new NonInstanceClassCreateException(EmailUtil.class);
    }

    public static boolean isEmailValid(String email) {
        return (email.length() == 0) || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
