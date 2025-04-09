package elbo.dotg.api17.domain.order;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.common.BaseTimeEntity;
import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "order", indexes = {
        @Index(name = "idx_order_id", columnList = "order_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @Column @OneToMany(mappedBy = "order")
    private List<Product> product;

    @Column(columnDefinition = "number default 0")
    private long price;

    @Column(columnDefinition = "number default 0")
    private long quantity;

    @Builder
    public Order(Long id, List<Product> product, long price, long quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
