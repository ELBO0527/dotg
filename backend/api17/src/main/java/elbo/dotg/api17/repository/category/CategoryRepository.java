package elbo.dotg.api17.repository.category;

import elbo.dotg.api17.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository{
}