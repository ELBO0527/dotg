package elbo.dotg.api17.service.board;

import elbo.dotg.api17.advice.exception.board.BoardNotFoundException;
import elbo.dotg.api17.advice.exception.category.CategoryNotFoundException;
import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.dto.request.board.SaveBoardRequest;
import elbo.dotg.api17.dto.request.board.UpdateBoardRequest;
import elbo.dotg.api17.dto.response.board.BoardResponse;
import elbo.dotg.api17.repository.board.BoardRepository;
import elbo.dotg.api17.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    public List<BoardResponse> findBoards(){
            List<Board> boards = boardRepository.findAll();
            return boards.stream().map(BoardResponse::from).collect(Collectors.toList());
    }

    public BoardResponse findBoardById(final long id){
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        board.updateViewCount();
        return BoardResponse.from(board);
    }

    public BoardResponse updateBoardById(final long id, final UpdateBoardRequest boardUpdateRequest){
        //findBoardById(id); <<이렇게 쓰기 위해선 toEntity?
        //boardRepository.findById(id).orElseThrow(BoardNotFoundException::new)
        findBoardById(id).toEntity().updateBoard(
                Board.builder()
                        .title(boardUpdateRequest.title())
                        .category(categoryRepository.findById(boardUpdateRequest.categoryId()).orElseThrow(CategoryNotFoundException::new))
                        .attachments(boardUpdateRequest.attachments())
                        .content(boardUpdateRequest.Content())
                .build());
        return BoardResponse.from(boardRepository.findById(id).orElseThrow(BoardNotFoundException::new));
    }

    public BoardResponse saveBoard(final SaveBoardRequest saveBoardRequest){

        return BoardResponse.from(boardRepository.save(Board.builder()
                        .title(saveBoardRequest.title())
                        .attachments(saveBoardRequest.attachments())
                        .content(saveBoardRequest.content())
                        .category(categoryRepository.findById(saveBoardRequest.categoryId()).orElseThrow(CategoryNotFoundException::new))
                        .build()));
    }

    public long deleteBoardById(final long id){
        boardRepository.deleteById(id);
        return id;
    }
}
