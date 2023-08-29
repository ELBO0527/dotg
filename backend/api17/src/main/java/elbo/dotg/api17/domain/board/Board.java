package elbo.dotg.api17.domain.board;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.comment.Comment;
import elbo.dotg.api17.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private int viewCount;

    @Column
    private ArrayList<String> attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, int viewCount, List<Comment> comments, ArrayList<String> attachments, Category category, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.comments = comments;
        this.attachments = attachments;
        this.category = category;
        this.user = user;
    }
}
