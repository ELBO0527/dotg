package elbo.dotg.api17.repository.category;

import elbo.dotg.api17.domain.category.Category;

import java.util.List;

public interface CategoryCustomRepository {
    List<Category> findAllInnerFetchJoin();

    List<Category> findAllInnerFetchJoinWithDistinct();
}
