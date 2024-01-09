package elbo.dotg.api17.domain.board;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.comment.Comment;
import elbo.dotg.api17.domain.common.BaseTimeEntity;
import elbo.dotg.api17.domain.user.User;
import elbo.dotg.api17.dto.request.board.UpdateBoardRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board", indexes = {
        @Index(name = "idx_board_id", columnList = "board_id"),
        @Index(name = "idx_board_title", columnList = "title"),
        @Index(name = "idx_board_name", columnList = "content")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @Column
    private String title;

    @Column//blob으로 변경
    private String content;

    @Column(columnDefinition = "number default 0")
    private long viewCount;

    @Column//갯수 제한 걸기
    private List<String> attachments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, long viewCount, List<Comment> comments, List<String> attachments, Category category, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.comments = comments;
        this.attachments = attachments;
        this.category = category;
        this.user = user;
    }

    public void updateBoard(Board board){
        this.title = board.getTitle();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.attachments = board.getAttachments();
        this.viewCount = board.getViewCount();
        this.comments = board.getComments();
        this.user = board.getUser();
    }

    public void updateViewCount(){
        this.viewCount++;
    }
}
