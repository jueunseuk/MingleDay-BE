package returns.mingleday.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.category.CategoryResponse;
import returns.mingleday.model.category.UpsertCategoryRequest;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.category.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{mingleId}/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId) {
        List<CategoryResponse> response = categoryService.getAllCategoryByMingle(user.getUserId(), mingleId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<String>> createCategory(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @RequestBody UpsertCategoryRequest request) {
        categoryService.createCategory(user.getUserId(), mingleId, request);
        return ResponseEntity.ok(SuccessResponse.success("Category created successfully"));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<SuccessResponse<String>> modifyCategory(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @PathVariable Long categoryId, @RequestBody UpsertCategoryRequest request) {
        categoryService.modifyCategory(user.getUserId(), mingleId, categoryId, request);
        return ResponseEntity.ok(SuccessResponse.success("Success to modify category"));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<SuccessResponse<String>> deleteCategory(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @PathVariable Long categoryId) {
        categoryService.deleteCategory(user.getUserId(), mingleId, categoryId);
        return ResponseEntity.ok(SuccessResponse.success("Success to delete category"));
    }
}
