package selecting.platform.error.response;

import lombok.Builder;
import lombok.Getter;
import selecting.platform.error.ErrorCode;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private int status;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode.getErrorCode();
        this.message = message;
    }


    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .errorCode(errorCode.name())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }


}
