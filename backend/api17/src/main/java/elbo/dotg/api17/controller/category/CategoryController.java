package elbo.dotg.api17.controller.category;

import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> saveParentCategory(@RequestBody SaveCategoryRequest saveCategoryRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveParentCategory(saveCategoryRequest));
    }
}
