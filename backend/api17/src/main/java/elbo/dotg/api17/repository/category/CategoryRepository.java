package elbo.dotg.api17.repository.category;

import elbo.dotg.api17.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {
    Optional<Category> findByName(String name);
}
