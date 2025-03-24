package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
