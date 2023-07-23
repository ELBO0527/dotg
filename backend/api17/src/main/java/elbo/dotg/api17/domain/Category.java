package elbo.dotg.api17.domain;

import elbo.dotg.api17.domain.Board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name ="parent_id")
    private Category parent;
    @OneToMany (mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @Builder
    public Category(Long id, String name, Category parent, List<Category> children, Board board) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.children = children;
        this.board = board;
    }
}
