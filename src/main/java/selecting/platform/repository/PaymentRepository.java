package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import selecting.platform.model.Payment;
import selecting.platform.model.Reservation;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByReservation(Reservation reservation);
}
