package elbo.dotg.api17.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elbo.dotg.api17.dto.request.board.SaveBoardRequest;
import elbo.dotg.api17.dto.request.order.OrderRequest;
import elbo.dotg.api17.dto.response.board.BoardResponse;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.service.board.BoardService;
import elbo.dotg.api17.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final KafkaTemplate<String, String> kafka;

    @PostMapping
    public ApiResponse<String> makeOrder(final OrderRequest orderRequest) throws JsonProcessingException {
        kafka.send("order-topic", new ObjectMapper().writeValueAsString(orderRequest));
        return success("메세지 발송 성공");
    }
}
