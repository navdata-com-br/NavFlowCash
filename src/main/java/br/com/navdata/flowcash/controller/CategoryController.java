package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.request.CategoryRequest;
import br.com.navdata.flowcash.dto.response.CategoryResponse;
import br.com.navdata.flowcash.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(categoryService.listByUser(userId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CategoryRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(userId, req));
    }

    @PostMapping("/seed")
    public ResponseEntity<Void> seed(@RequestHeader("X-User-Id") String userId) {
        categoryService.seedDefaultCategories(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String id
    ) {
        categoryService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
