package elbo.dotg.api17.domain.category;

import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.common.BaseTimeEntity;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category", indexes = @Index(name = "i_category_name", columnList = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    private Category(String name, CategoryType categoryType, Category parent) {
        this.name = name;
        this.categoryType = categoryType;
        this.parent = parent;
    }

    public static Category of(String name, CategoryType categoryType, Category parent) {
        return new Category(name, categoryType, parent);
    }

    private Category(Long id, String name, CategoryType categoryType, Category parent) {
        this.id = id;
        this.name = name;
        this.categoryType = categoryType;
        this.parent = parent;
    }

    public static Category of(Long id, String name, CategoryType categoryType, Category parent) {
        return new Category(id, name, categoryType, parent);
    }

    public void setCategory(String name, CategoryType categoryType) {
        this.name = name;
        this.categoryType = categoryType;
    }
}
