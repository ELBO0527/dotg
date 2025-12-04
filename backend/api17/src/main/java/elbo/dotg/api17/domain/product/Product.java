package elbo.dotg.api17.domain.product;

import elbo.dotg.api17.domain.common.BaseTimeEntity;
import elbo.dotg.api17.domain.order.Orders;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column
    private long price;

    @Column
    private long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;*/

    @Builder
    public Product(Long id, String name, long price, long quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        //this.category = category;
        //this.user = user;
    }

    public void validQuantity(Long quantity) {
        if (quantity > this.quantity){
            throw new IllegalStateException("재고는 0보다 작을 수 없습니다.");
        }
    }

    public void decrease(){
        if (quantity <= 0){
            throw new IllegalStateException("재고는 0보다 작을 수 없습니다.");

        }
        quantity -= 1;
    }
}
