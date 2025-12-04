package elbo.dotg.api17.dto.response.product;

import elbo.dotg.api17.domain.order.Orders;
import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.dto.response.order.OrderResponse;

public record ProductResponse(String name, long price, long quantity) {

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getName(),
                product.getPrice(),
                product.getQuantity());
    }
}
