package elbo.dotg.api17.service.board;

import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.domain.comment.Comment;
import elbo.dotg.api17.domain.user.User;
import elbo.dotg.api17.dto.request.board.SaveBoardRequest;
import elbo.dotg.api17.dto.response.board.BoardResponse;
import elbo.dotg.api17.repository.board.BoardRepository;
import elbo.dotg.api17.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * boardService로 통테 연습
 */
@PropertySource("classpath:/dev-secure.properties")
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired CategoryRepository categoryRepository;

    @DisplayName("게시물_전체_조회")
    @Test
    void findboards() throws Exception {
        //given
        int repeat = 10;
        StringBuilder title = new StringBuilder("title");
        StringBuilder content = new StringBuilder("content");

        //Category category = Category.of("name", CategoryType.BOARD_COMMON, null);
        //User user = User.builder().build();

        for (int i=0;i<repeat;i++){
            boardRepository.save(new Board(Long.valueOf(i), title.toString() , content.toString() , 0, null, null , null, null));
        }

        //when
        List<BoardResponse> boards = boardService.findBoards();

        //then
        assertEquals(repeat, boards.size());
    }

    @Test
    void find_a_single_board_by_id_with_increase_viewcount() throws Exception {
        //given
        long boardId = 49952L;
        String boardTitle = "boardTitle";
        String boardContent = "boardContent";
        long viewCount = 0L;
        List<Comment> commtents = List.of(Comment.builder().build());
        List<String> attachments = List.of("attechmentA","attechmentB","attechmentC");
        Category category = Category.of(1L,"wantToStopCreatingSomething", CategoryType.BOARD_COMMON, null );
        User phantom = User.builder().build();

        //when
        BoardResponse findBoard = boardService.findBoardById(boardId);

        //then
        assertEquals(boardId, findBoard.id());
        assertEquals(viewCount + 1, findBoard.viewCount());
    }

    @Test
    void update_a_board_by_id() throws Exception {
        //given

        //when

        //then
    }

    @Test
    void create_a_board() throws Exception {
        //given

        //when

        //then
    }

    @Test
    void delete_board_by_id() throws Exception {
        //given

        //when

        //then
    }
}