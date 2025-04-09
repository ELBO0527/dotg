package elbo.dotg.api17.service.order;

import elbo.dotg.api17.advice.exception.board.BoardNotFoundException;
import elbo.dotg.api17.advice.exception.category.CategoryNotFoundException;
import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.order.Order;
import elbo.dotg.api17.dto.request.board.SaveBoardRequest;
import elbo.dotg.api17.dto.request.board.UpdateBoardRequest;
import elbo.dotg.api17.dto.response.board.BoardResponse;
import elbo.dotg.api17.dto.response.order.OrderResponse;
import elbo.dotg.api17.repository.board.BoardRepository;
import elbo.dotg.api17.repository.category.CategoryRepository;
import elbo.dotg.api17.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;

    public List<OrderResponse> findOrders(){
            List<Order> orders = orderRepository.findAll();
            return orders.stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    public OrderResponse findOrderById(final long id){
        Order order = orderRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        return OrderResponse.from(order);
    }

    public OrderResponse updateOrderById(final long id, final UpdateBoardRequest boardUpdateRequest){
        //findBoardById(id); <<이렇게 쓰기 위해선 toEntity?
        //boardRepository.findById(id).orElseThrow(BoardNotFoundException::new)
//        findBoardById(id).toEntity().updateBoard(
//                Board.builder()
//                        .title(boardUpdateRequest.title())
//                        .category(categoryRepository.findById(boardUpdateRequest.categoryId()).orElseThrow(CategoryNotFoundException::new))
//                        .attachments(boardUpdateRequest.attachments())
//                        .content(boardUpdateRequest.Content())
//                .build());
        return OrderResponse.from(orderRepository.findById(id).orElseThrow(BoardNotFoundException::new));
    }

    /*public BoardResponse orderBoard(final SaveBoardRequest saveBoardRequest){
        return BoardResponse.from(orderRepository.save(Board.builder()
                        .title(saveBoardRequest.title())
                        .attachments(saveBoardRequest.attachments())
                        .content(saveBoardRequest.content())
                        .category(categoryRepository.findById(saveBoardRequest.categoryId()).orElseThrow(CategoryNotFoundException::new))
                        .build()));
    }*/

    public long cancleOrderById(final long id){
        orderRepository.deleteById(id);
        return id;
    }
}
