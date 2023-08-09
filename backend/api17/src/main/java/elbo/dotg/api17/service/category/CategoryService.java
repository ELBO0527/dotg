package elbo.dotg.api17.service.category;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long saveParentCategory(SaveCategoryRequest saveCategoryRequest){
        Category category = categoryRepository.save(
                Category.builder()
                    .name(saveCategoryRequest.name())
                    .categoryType(CategoryType.BOARD)
                .build());

        return category.getId();
    }
}
