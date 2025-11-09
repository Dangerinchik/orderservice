package rainchik.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "items", indexes = {
        @Index(name = "idx_items_name", columnList = "name"),
        @Index(name = "idx_items_price", columnList = "price")
})
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "price", length = 20)
    private String price;

}
