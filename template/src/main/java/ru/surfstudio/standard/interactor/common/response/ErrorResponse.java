package ru.surfstudio.standard.interactor.common.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * базовый интерфейс ответа
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements BaseResponse {

    @SerializedName("userMessage")
    private String userMessage;
    @SerializedName("errorCode")
    private int errorCode;
}
