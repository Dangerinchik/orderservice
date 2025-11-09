package rainchik.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rainchik.orderservice.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
