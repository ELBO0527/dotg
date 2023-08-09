package elbo.dotg.api17.dto.request.category;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import lombok.Builder;

@Builder
public record SaveCategoryRequest(String name, CategoryType categoryType) {
    public static SaveCategoryRequest from(Category category){
        return SaveCategoryRequest.builder()
                .name(category.getName())
                .categoryType(category.getCategoryType())
                .build();
    }
}
