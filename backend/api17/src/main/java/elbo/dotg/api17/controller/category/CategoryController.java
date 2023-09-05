package elbo.dotg.api17.controller.category;

import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.dto.response.category.CategoryResponse;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> findAllCategories(){
        return success(categoryService.findAllCategories());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> saveParentCategory(@RequestBody SaveCategoryRequest saveCategoryRequest){
        return success(categoryService.saveCategory(saveCategoryRequest));
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable long id, @RequestBody SaveCategoryRequest saveCategoryRequest){
        return success(categoryService.updateCategory(id, saveCategoryRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Long> deleteCategory(@PathVariable long id){
        return success(categoryService.deleteCategory(id));
    }
}
