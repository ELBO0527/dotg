package elbo.dotg.api17.dto.request.category;

import elbo.dotg.api17.domain.category.CategoryType;

public record SaveCategoryRequest(
        String name,
        CategoryType categoryType,
        Long parentId) {
}
