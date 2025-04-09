package elbo.dotg.api17.dto.response.order;

import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.order.Order;
import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.dto.response.board.BoardResponse;

import java.util.List;

public record OrderResponse(Long id, List<Product> product, long price, long quantity) {
    public static OrderResponse from(final Order order) {
        return new OrderResponse(order.getId(),
                order.getProduct(),
                order.getPrice(),
                order.getQuantity());
    }

}

