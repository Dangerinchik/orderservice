package rainchik.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rainchik.orderservice.entity.Order;
import rainchik.orderservice.entity.Status;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
//я хочу попрактиковаться в написании запросов, поэтому буду все писать вручную)

    @Modifying
    @Query(value = "INSERT INTO orders (user_id, status, creation_date) " +
            "VALUES (:#{order.userId}, :#{order.status}, NOW())", nativeQuery = true)
    public void create(@Param("order") Order order);

    @Query(value = "SELECT o FROM Order o WHERE o.id IN :ids")
    public List<Order> findByIdIn(@Param("ids") List<Long> orderIds);

    @Query(value = "SELECT * FROM orders WHERE id = :id", nativeQuery = true)
    public Optional<Order> findByOrderId(@Param("id") Long orderId);

    @Query(value = "SELECT o FROM Order o WHERE o.status IN :statuses")
    public List<Order> findByStatuses(@Param("statuses") List<String> statuses);

    @Modifying
    @Query(value = "UPDATE orders SET user_id = :userId, status = :status " +
            "WHERE id = :id", nativeQuery = true)
    void updateOrder(@Param("id") Long orderId, @Param("userId") Long userId, @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE orders SET status = :status " +
            "WHERE id = :id", nativeQuery = true)
    void updateOrderStatus(@Param("id") Long orderId, @Param("status") String status);

    @Modifying
    @Query(value = "DELETE FROM Order o WHERE o.id = :id")
    public void deleteOrder(@Param("id") Long orderId);

}
