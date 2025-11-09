package rainchik.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rainchik.orderservice.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
