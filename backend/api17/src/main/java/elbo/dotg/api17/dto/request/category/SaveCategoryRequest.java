package elbo.dotg.api17.dto.request.category;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.dto.response.category.CategoryResponse;
import lombok.Builder;

public record SaveCategoryRequest(String name, CategoryType categoryType, Long parentId) {
}
