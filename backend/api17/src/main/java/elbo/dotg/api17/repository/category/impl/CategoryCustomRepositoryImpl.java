package elbo.dotg.api17.repository.category.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.repository.category.CategoryCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Category> findAllInnerFetchJoin() {
        return null;
    }

    @Override
    public List<Category> findAllInnerFetchJoinWithDistinct() {
        return null;
    }
}
