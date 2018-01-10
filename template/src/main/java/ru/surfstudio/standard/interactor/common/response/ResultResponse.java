package ru.surfstudio.standard.interactor.common.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответ от сервера содержащий результат
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> extends ErrorResponse {

    @SerializedName("result")
    private T result;
}
