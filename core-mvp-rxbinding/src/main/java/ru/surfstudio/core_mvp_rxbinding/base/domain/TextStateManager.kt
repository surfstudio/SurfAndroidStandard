package ru.surfstudio.core_mvp_rxbinding.base.domain

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Менеджер состояний для [EditText] с меняющимся текстом
 *
 * Содержит в себе [State] и [Action] для хранения и обработки ввода текста
 */
class TextStateManager(initialText: String = EMPTY_STRING) {
    val textState = State(initialText)
    val textChangedAction = Action<String>()

    init {
        textChangedAction.relay
                .filter { it != textState.value }
                .subscribe {
                    textState.relay.accept(it)
                }
    }

    /**
     * Подписка менеджера состояний к определенному [EditText]
     */
    fun bind(
            editText: EditText,
            compositeDisposable: CompositeDisposable
    ) {

        var editing = false

        compositeDisposable.addAll(
                textState.observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val editable = editText.text
                            if (it?.contentEquals(editable) != true) {
                                editing = true
                                if (editable is Spanned) {
                                    val ss = SpannableString(it)
                                    TextUtils.copySpansFrom(editable, 0, ss.length, null, ss, 0)
                                    editable.replace(0, editable.length, ss)
                                } else {
                                    editable.replace(0, editable.length, it)
                                }
                                editing = false
                            }
                        },

                editText.textChanges()
                        .skipInitialValue()
                        .filter { !editing }
                        .map { it.toString() }
                        .subscribe(textChangedAction.consumer)
        )
    }


}