package elbo.dotg.api17.repository.order;

import elbo.dotg.api17.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
