package elbo.dotg.api17.dto.request.order;

import elbo.dotg.api17.domain.product.Product;

import java.util.List;

public record OrderRequest(Product product, long quantity) {
}
