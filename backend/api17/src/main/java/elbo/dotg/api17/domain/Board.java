package elbo.dotg.api17.domain;

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

    private String title;
    private String content;
    private int viewCount;

    @OneToMany(mappedBy = "board_id")
    private List<Comment> comments = new ArrayList<>();
    private ArrayList<String> attachments;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Board(Long id, String title, String content, int viewCount, List<Comment> comments, ArrayList<String> attachments, Category category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.comments = comments;
        this.attachments = attachments;
        this.category = category;
    }
}
