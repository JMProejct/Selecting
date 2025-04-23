package selecting.platform.error;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 🧱 [공통]
    COMMON_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 내부 오류가 발생했습니다."),
    COMMON_INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "400", "잘못된 요청 파라미터입니다."),
    COMMON_ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "요청한 리소스를 찾을 수 없습니다."),

    // 🔐 [인증/보안]
    AUTH_MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "100", "토큰이 존재하지 않습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "101", "유효하지 않은 토큰입니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "102", "만료된 토큰입니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "103", "접근 권한이 없습니다."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "104", "로그인이 필요한 서비스입니다."),

    // 🙍‍♂️ [사용자]
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "201", "해당 사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "202", "이미 존재하는 사용자입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "409", "이미 사용 중인 이메일입니다."),
    USER_INVALID_ROLE(HttpStatus.BAD_REQUEST, "203", "사용자 권한이 올바르지 않습니다."),
    ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "409", "이미 사용 중인 아이디입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "401", "비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "401", "아이디 또는 비밀번호가 올바르지 않습니다."),

    // 📚 [게시글 / 과외]
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "301", "해당 게시글을 찾을 수 없습니다."),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "302", "해당 게시글에 접근할 수 없습니다."),

    // 🗓️ [예약 / 결제]
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "401", "예약 정보를 찾을 수 없습니다."),
    PAYMENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "402", "결제 처리에 실패했습니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "RES-002" , "이미 확정된 예약은 다시 대기 상태로 변경할 수 없습니다."),
    RESERVATION_NOT_AVAILABLE(HttpStatus.CONFLICT, "RES-003", "해당 시간은 예약할 수 없습니다."),
    CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RES-004", "수업 시작 24시간 이내에는 취소가 불가능합니다."),
    ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "RES-005", "이미 취소된 예약입니다."),

    // 🔔 [알림]
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "알림을 찾을 수 없습니다."),

    // 💰 [결제]
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAY-001", "결제 정보를 찾을 수 없습니다."),

    // 📝 [리뷰]
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "501", "리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "502", "이미 리뷰를 작성하였습니다."),

    // 📁 [파일 업로드]
    FILE_INVALID_TYPE(HttpStatus.BAD_REQUEST, "601", "허용되지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "602", "파일 크기 제한을 초과했습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "603", "파일 업로드에 실패했습니다."),

    // 💬 [채팅]
    CHATROOM_NOT_FOUNT(HttpStatus.BAD_REQUEST,"701","채팅방이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
