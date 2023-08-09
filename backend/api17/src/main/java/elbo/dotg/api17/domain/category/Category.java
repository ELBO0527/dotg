package elbo.dotg.api17.domain.category;

import elbo.dotg.api17.domain.board.Board;
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

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name ="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "category")
    private List<Board> boards = new ArrayList<>();

    @OneToMany (mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @Builder
    public Category(Long id, String name, CategoryType categoryType, Category parent, List<Board> boards, List<Category> children) {
        this.id = id;
        this.name = name;
        this.categoryType = categoryType;
        this.parent = parent;
        this.boards = boards;
        this.children = children;
    }
}
