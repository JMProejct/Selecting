package selecting.platform.error.response;

import lombok.Getter;
import selecting.platform.error.ErrorCode;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.code = errorCode.getErrorCode();
        this.message = message;
    }


}
