package elbo.dotg.api17.service.category;

import elbo.dotg.api17.advice.exception.category.CategoryNotFoundException;
import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.dto.response.category.CategoryResponse;
import elbo.dotg.api17.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 카테고리를 무한으로 즐겨보자
 *
 * @AUTHOR 나
 * @Exception CategoryNotFoundException
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(cacheNames = "categoryCache")
    public List<CategoryResponse> findAllCategories() {
        return categoryRepository.findAllInnerFetchJoin().stream().map(CategoryResponse::from).collect(Collectors.toList());
    }

    public CategoryResponse findByCategoryId(final long categoryId) {
        return CategoryResponse.from(categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new));
    }

    @Transactional
    public Long saveCategory(final SaveCategoryRequest saveCategoryRequest) {
        Category category = Category.of(saveCategoryRequest.name(), CategoryType.BOARD_COMMON,
                saveCategoryRequest.parentId() == null ? null :
                        categoryRepository.findById(saveCategoryRequest.parentId()).orElseThrow(() -> new RuntimeException("못 찾겠당")));
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public CategoryResponse updateCategory(final long categoryId, final SaveCategoryRequest saveCategoryRequest) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        category.setCategory(saveCategoryRequest.name(), CategoryType.BOARD_COMMON);
        return CategoryResponse.from(category);
    }

    @Transactional
    public long deleteCategory(final long id) {
        categoryRepository.findById(id);
        categoryRepository.deleteById(id);
        return id;
    }

    /**
     * 람다식 써보려고 만든 함수. 사용안함
     *
     * @param saveCategoryRequest
     * @return
     */
    @Transactional
    public Long saveParentCategoryV2(SaveCategoryRequest saveCategoryRequest) {
        Category category2 = Category.of(null, saveCategoryRequest.name(), saveCategoryRequest.categoryType(), null);
        return categoryRepository.findByName(saveCategoryRequest.name())
                .map(Category::getId)
                .orElseGet(() -> categoryRepository.save(category2).getId());
    }
}
