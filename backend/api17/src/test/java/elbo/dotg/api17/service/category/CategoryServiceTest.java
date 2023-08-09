package elbo.dotg.api17.service.category;

import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.repository.category.CategoryRepository;
import elbo.dotg.api17.repository.user.UserRepository;
import elbo.dotg.api17.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_Parent_Category_Test() {
        SaveCategoryRequest saveCategoryRequest = new SaveCategoryRequest("테스트를 해보자", CategoryType.BOARD);

        categoryService.saveParentCategory(saveCategoryRequest);

        verify(categoryRepository, times(1)).save(any());
    }

}