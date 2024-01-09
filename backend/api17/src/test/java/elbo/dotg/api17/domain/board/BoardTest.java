package elbo.dotg.api17.domain.board;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.domain.comment.Comment;
import elbo.dotg.api17.domain.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


class BoardTest {

    @Test
    void update_view_count_test() throws Exception {
        //given
        int repeat = 100;
        Board examBoard = new Board(1L, "Board_Title", "Board_Content", 0L, List.of(Comment.builder().build()),
                List.of("Attachment1", "Attachment2"), Category.of("Test_Cate", CategoryType.BOARD_COMMON, null),
                User.builder().build());

        //when
        for (int i=0; i<repeat; i++){
            examBoard.updateViewCount();
        }

        //then
        assertThat(examBoard).isNotNull();
        assertThat(repeat).isEqualTo(examBoard.getViewCount());
    }
}