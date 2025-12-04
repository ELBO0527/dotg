package elbo.dotg.api17.dto.response.order;

import elbo.dotg.api17.domain.order.Orders;
import elbo.dotg.api17.domain.product.Product;

import java.util.List;

public record OrderResponse(Long id, List<Product> product, long price, long quantity) {
    public static OrderResponse from(final Orders orders) {
        return new OrderResponse(orders.getId(),
                orders.getProduct(),
                orders.getPrice(),
                orders.getQuantity());
    }

}

