package elbo.dotg.api17.dto.response.category;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public record CategoryResponse(String name,
                               CategoryType categoryType,
                               List<CategoryResponse> children) {
    public static CategoryResponse from(Category category){
        return new CategoryResponse(category.getName(),
                                    category.getCategoryType(),
                                    category.getChildren().stream().map(CategoryResponse::from).collect(Collectors.toList()));
    }
}
