package elbo.dotg.api17.domain.product;

import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.comment.Comment;
import elbo.dotg.api17.domain.common.BaseTimeEntity;
import elbo.dotg.api17.domain.order.Order;
import elbo.dotg.api17.domain.user.User;
import elbo.dotg.api17.dto.request.board.UpdateBoardRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(columnDefinition = "number default 0")
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;*/

    @Builder
    public Product(Long id, String name, long price, Category category, User user) {
        this.id = id;
        this.name = name;
        this.price = price;
        //this.category = category;
        //this.user = user;
    }
}
