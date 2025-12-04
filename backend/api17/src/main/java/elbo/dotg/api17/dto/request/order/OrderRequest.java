package elbo.dotg.api17.dto.request.order;

import elbo.dotg.api17.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderRequest {
    Product product;
    List<Long> productIds;
    long quantity;
}
