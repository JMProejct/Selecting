package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
