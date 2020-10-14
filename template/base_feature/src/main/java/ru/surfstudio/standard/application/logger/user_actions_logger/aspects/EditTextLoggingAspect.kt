@file:Suppress("unused")

package ru.tricolor.android.application.logger.user_actions_logger.aspects

import android.text.SpannableStringBuilder
import android.widget.EditText
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.*
import ru.tricolor.android.application.logger.user_actions_logger.ViewInput

/**
 * Аспект, отвечающий за логгирование событий с EditText
 */
@Aspect
class EditTextLoggingAspect : BaseAspect() {

    private var editTextList = mutableListOf<EditText>()

    /**
     * Обработчик, вызываемый при добавлении листнера ввода текста
     *
     * @param context - информация о точке вызова метода
     */
    @Before("call(void android.widget.TextView.addTextChangedListener(..)) && args(..)")
    fun onAddTextChangedListenerAdvice(context: JoinPoint) {
        val editText = context.target as EditText
        val list = editTextList.filter { savedEditText ->
            with(editText) {
                resources.getResourceEntryName(editText.id) == resources.getResourceEntryName(savedEditText.id)
            }
        }

        if (list.isEmpty()) {
            editTextList.add(editText)
        }
    }

    /**
     * Обработчик, логгирующий вводимый текст
     *
     * @param inputtedString - вводимая строка
     */
    @After("execution(void *.onTextChanged(..)) && args(inputtedString,..)")
    fun onTextChangesAdvice(inputtedString: SpannableStringBuilder) {
        val editText = editTextList.find { it.text == inputtedString }
        editText?.let { log(ViewInput(it).toString()) }
    }

    /**
     * Обработчик, очищающий список текущих обрабатываемых EditText
     */
    @After("execution(void ru.surfstudio.android.core.ui.activity.CoreActivity.onDestroy())")
    fun onClearEditTextListAdvice() {
        editTextList.clear()
    }
}