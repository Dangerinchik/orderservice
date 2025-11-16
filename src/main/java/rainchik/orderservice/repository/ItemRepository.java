package rainchik.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rainchik.orderservice.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    public List<Item> findByName(String name);

}
