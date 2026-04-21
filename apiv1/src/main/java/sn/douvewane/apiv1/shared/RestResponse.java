package sn.douvewane.apiv1.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestResponse<T>(
        String status,
        String message,
        T data,
        LocalDateTime timestamp
) {
    public RestResponse(String status, String message, T data) {
        this(status, message, data, LocalDateTime.now());
    }

    public static <T> RestResponse<T> success(T data) {
        return new RestResponse<>("SUCCESS", "Opération réussie", data);
    }

    public static <T> RestResponse<T> success(T data, String message) {
        return new RestResponse<>("SUCCESS", message, data);
    }

    public static <T> RestResponse<T> error(String message) {
        return new RestResponse<>("ERROR", message, null);
    }

    public static <T> RestResponse<T> error(String message, T data) {
        return new RestResponse<>("ERROR", message, data);
    }
}
