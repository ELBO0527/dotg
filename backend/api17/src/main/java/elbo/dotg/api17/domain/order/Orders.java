package elbo.dotg.api17.domain.order;

import elbo.dotg.api17.domain.common.BaseTimeEntity;
import elbo.dotg.api17.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_id", columnList = "orders_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orders_id")
    private Long id;

    @Column @OneToMany(mappedBy = "orders")
    private List<Product> product;

    @Column
    private long price;

    @Column
    private long quantity;

    @Builder
    public Orders(Long id, List<Product> product, long price, long quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
