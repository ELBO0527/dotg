package elbo.dotg.api17.domain.comment;

import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment", indexes = @Index(name = "i_comment_content", columnList = "content"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, Board board, String content) {
        this.id = id;
        this.board = board;
        this.content = content;
    }
}
