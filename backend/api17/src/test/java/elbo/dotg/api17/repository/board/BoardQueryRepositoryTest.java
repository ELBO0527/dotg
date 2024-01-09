package elbo.dotg.api17.repository.board;

import elbo.dotg.api17.domain.board.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@ExtendWith(SpringExtension.class)
class BoardQueryRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardQueryRepository boardQueryRepository;

    @Test
    void queryDsl_기본_성능_확인() throws Exception {
        //given
        String title = "board_title";
        boardRepository.save(Board.builder().title(title).build());

        //when
        List<Board> result =  boardQueryRepository.findBySearchParam(title);

        //then
        assertEquals(result.size(), 5);
        assertEquals(result.get(0).getTitle(), title);
    }

}