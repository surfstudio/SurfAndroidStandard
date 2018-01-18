package ru.surfstudio.standard.interactor.auth.network.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.core.util.Transformable
import ru.surfstudio.standard.domain.auth.recover.RecoverByEmailStatus
import ru.surfstudio.standard.interactor.common.network.error.InvalidServerValuesResponse

/**
 * ответ от сервера при попытке восстановления доступа через почту
 */

data class RecoverByEmailResponse(@SerializedName("email") private val email: String?) : Transformable<RecoverByEmailStatus> {
    override fun transform(): RecoverByEmailStatus {
        if (email == null) {
            throw InvalidServerValuesResponse(Pair("email", "null"))
        }
        return RecoverByEmailStatus(email)
    }
}