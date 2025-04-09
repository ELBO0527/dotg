package elbo.dotg.api17.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import elbo.dotg.api17.advice.exception.board.BoardNotFoundException;
import elbo.dotg.api17.advice.exception.category.CategoryNotFoundException;
import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.dto.request.board.SaveBoardRequest;
import elbo.dotg.api17.dto.request.board.UpdateBoardRequest;
import elbo.dotg.api17.dto.response.board.BoardResponse;
import elbo.dotg.api17.repository.board.BoardRepository;
import elbo.dotg.api17.repository.category.CategoryRepository;
import elbo.dotg.api17.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @KafkaListener(topics = "order-topic", groupId = "topic-group-01")
    public void Order(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(message);

        String name = node.get("name").asText();
        int quantity = node.get("quantity").asInt();

        log.info("send message : {}", name);
        log.info("send key : {}", quantity);
    }

   /* public List<BoardResponse> findBoards(){
            List<Product> products = productRepository.findAll();
            return products.stream().map(BoardResponse::from).collect(Collectors.toList());
    }*/

    /*public BoardResponse findBoardById(final long id){
        Product product = productRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        return BoardResponse.from(product);
    }*/

    /*public BoardResponse updateProductById(final long id, final UpdateBoardRequest boardUpdateRequest){
        findBoardById(id).toEntity().updateBoard(
                Board.builder()
                        .title(boardUpdateRequest.title())
                        .category(categoryRepository.findById(boardUpdateRequest.categoryId()).orElseThrow(CategoryNotFoundException::new))
                        .attachments(boardUpdateRequest.attachments())
                        .content(boardUpdateRequest.Content())
                .build());
        return BoardResponse.from(boardRepository.findById(id).orElseThrow(BoardNotFoundException::new));
    }*/

    /*public BoardResponse saveBoard(final SaveBoardRequest saveBoardRequest){
        return BoardResponse.from(boardRepository.save(Board.builder()
                        .title(saveBoardRequest.title())
                        .attachments(saveBoardRequest.attachments())
                        .content(saveBoardRequest.content())
                        .category(categoryRepository.findById(saveBoardRequest.categoryId()).orElseThrow(CategoryNotFoundException::new))
                        .build()));
    }*/

    /*public long deleteBoardById(final long id){
        boardRepository.deleteById(id);
        return id;
    }*/
}
