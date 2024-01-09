package elbo.dotg.api17.service.category;

import elbo.dotg.api17.advice.exception.category.CategoryNotFoundException;
import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.dto.response.category.CategoryResponse;
import elbo.dotg.api17.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void find_all_categories() throws Exception {
        //given
        List<Category> categoryList = new ArrayList<>();

        for (int i=0; i < 1000 ; i++){
            Category category = Category.of("성능_테스트_카테고리"+i+"번", CategoryType.BOARD_COMMON, null );
            categoryList.add(category);
        }
        when(categoryRepository.findAll()).thenReturn(categoryList);

        //when
        StopWatch stopWatch = new StopWatch("dotg_find_all_category");
        stopWatch.start("findAllCategories");

        List<CategoryResponse> categories = categoryService.findAllCategories();

        stopWatch.stop();
        long elapsedTime = stopWatch.getTotalTimeMillis();
        System.out.println("총 실행 시간: " + elapsedTime + "ms");
        System.out.println(stopWatch.prettyPrint());

        //then
    }

    @Test
    void save_parent_category() {
        SaveCategoryRequest saveCategoryRequest = new SaveCategoryRequest("테스트를 해보자", CategoryType.BOARD_COMMON, null);

        categoryService.saveCategory(saveCategoryRequest);

        verify(categoryRepository, times(1)).save(any());


    }

    @Test
    void save_child_category() throws Exception {

        Long parentId = 1L;
        Category category = Category.of( parentId, "부모_카테고리", CategoryType.BOARD_COMMON, null);
        //categoryService.saveCategory(category);

        //given
        SaveCategoryRequest saveChildCategoryRequest = new SaveCategoryRequest("자식_카테고리_테스트", CategoryType.BOARD_COMMON, category.getId());
        //when
        categoryService.saveCategory(saveChildCategoryRequest);

        //then
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void update_category() throws Exception {
        //given
        SaveCategoryRequest saveCategoryRequest = new SaveCategoryRequest("수정_카테고리", CategoryType.BOARD_COMMON, null);

        Category category = Category.of("수정_전_카테고리", CategoryType.BOARD_COMMON, null);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        //when
        StopWatch stopWatch = new StopWatch("dotg_update_category");
        stopWatch.start("updateCategory");

        categoryService.updateCategory(anyLong(), saveCategoryRequest);

        stopWatch.stop();
        long elapsedTime = stopWatch.getTotalTimeMillis();
        System.out.println("총 실행 시간: " + elapsedTime + "ms");
        System.out.println(stopWatch.prettyPrint());

        //then
        assertEquals(category.getName(), saveCategoryRequest.name());
        assertEquals(category.getCategoryType(), saveCategoryRequest.categoryType());
        assertNull(category.getParent());
    }

    @Test
    void find_category_by_categoryId() throws Exception {
        //given
        long categoryId = 1L;
        Category parentCategory = Category.of(1L, "부모 카테고리", CategoryType.BOARD_COMMON, null);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(parentCategory));
        //when
        CategoryResponse parentCategoryResponse = categoryService.findByCategoryId(1L);

        //then
        assertEquals(parentCategory.getName(), parentCategoryResponse.name());
        assertEquals(parentCategory.getCategoryType(), parentCategoryResponse.categoryType());
        assertEquals(parentCategory.getChildren(), parentCategoryResponse.children());

        System.out.println(parentCategoryResponse);
        System.out.println(parentCategory.getName());
    }

    @Test
    void throw_category_notFound_exception() throws Exception {
        //given
        long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(CategoryNotFoundException.class, ()->categoryService.findByCategoryId(categoryId));
    }

    @Test
    void delete_ParentCategory() throws Exception {
        long categoryId = 1L;
        categoryService.deleteCategory(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}