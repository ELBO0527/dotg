package elbo.dotg.api17.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elbo.dotg.api17.dto.request.order.OrderRequest;
import elbo.dotg.api17.dto.request.product.ProductRequest;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.dto.response.product.ProductResponse;
import elbo.dotg.api17.service.order.OrderService;
import elbo.dotg.api17.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafka;

    @GetMapping
    public ApiResponse<List<ProductResponse>> findAllProducts() {
        return success(productService.findAllPRoducts());
    }
    @PostMapping
    public ApiResponse<ProductResponse> saveProduct(@RequestBody final ProductRequest productRequest) {
        return success(productService.saveProduct(productRequest));
    }
}
