package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.Reservation;
import selecting.platform.model.User;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByPost_User(User user);

    List<Reservation> findByStudent(User student);
}
