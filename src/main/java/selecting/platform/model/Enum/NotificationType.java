package selecting.platform.model.Enum;


public enum NotificationType {

    RESERVATION_CANCELLED("📢", "학생이 수업 예약을 취소했습니다."),
    RESERVATION_APPROVED("✅", "예약이 승인되었습니다."),
    RESERVATION_REJECTED("❌", "예약이 거절되었습니다.");

    private final String emoji;
    private final String description;

    NotificationType(String emoji, String description) {
        this.emoji = emoji;
        this.description = description;
    }

    @Override
    public String toString() {
        return emoji + " " + description;
    }
}
