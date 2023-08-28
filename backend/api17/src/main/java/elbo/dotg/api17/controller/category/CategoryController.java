package elbo.dotg.api17.controller.category;

import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.dto.response.category.CategoryResponse;
import elbo.dotg.api17.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<CategoryResponse>> findAllCategories(){
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> saveParentCategory(@RequestBody SaveCategoryRequest saveCategoryRequest){
        return ResponseEntity.ok(categoryService.saveCategory(saveCategoryRequest));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable long id, @RequestBody SaveCategoryRequest saveCategoryRequest){
        return ResponseEntity.ok(categoryService.updateCategory(id, saveCategoryRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteCategory(@PathVariable long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
