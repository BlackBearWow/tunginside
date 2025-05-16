package uman.tunginside.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.category.CategoryDeleteForm;
import uman.tunginside.domain.category.CategoryRegisterForm;
import uman.tunginside.domain.member.Member;
import uman.tunginside.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @PostMapping
    public String createCategory(@RequestBody @Validated CategoryRegisterForm categoryRegisterForm, @SessionAttribute Long member_id) {
        return categoryService.registerCategory(categoryRegisterForm, member_id);
    }

    @DeleteMapping
    public String deleteCategory(@SessionAttribute Long member_id, @RequestBody CategoryDeleteForm categoryDeleteForm) {
        return categoryService.deleteCategory(member_id, categoryDeleteForm.getAbbreviation());
    }
}
