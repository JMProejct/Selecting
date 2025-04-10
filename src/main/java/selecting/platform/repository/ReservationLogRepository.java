package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.ReservationLog;

public interface ReservationLogRepository extends JpaRepository<ReservationLog, Long> {
}
