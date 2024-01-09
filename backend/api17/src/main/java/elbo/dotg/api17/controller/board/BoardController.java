package elbo.dotg.api17.controller.board;

import elbo.dotg.api17.dto.request.board.SaveBoardRequest;
import elbo.dotg.api17.dto.request.board.UpdateBoardRequest;
import elbo.dotg.api17.dto.response.board.BoardResponse;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;


@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards")
@RestController
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ApiResponse<List<BoardResponse>> findBoards(){//후에 querydsl 추추추가푸가후가
        return success(boardService.findBoards());
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<BoardResponse> findBoardById(final long id){
        return success(boardService.findBoardById(id));
    }

    @PostMapping
    public ApiResponse<BoardResponse> saveBoard(final SaveBoardRequest saveBoardRequest){
        return success(boardService.saveBoard(saveBoardRequest));
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<BoardResponse> updateBoardById(final long id, final UpdateBoardRequest updateBoardRequest){
        return success(boardService.updateBoardById(id,updateBoardRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Long> deleteBoardById(final long id){
        return success(boardService.deleteBoardById(id));
    }
}
