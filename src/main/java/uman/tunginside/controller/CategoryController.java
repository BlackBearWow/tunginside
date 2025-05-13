package uman.tunginside.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.Category;
import uman.tunginside.domain.CategoryRegisterForm;
import uman.tunginside.domain.Member;
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
    public String createCategory(@RequestBody @Validated CategoryRegisterForm categoryRegisterForm, @SessionAttribute("member") Member member) {
        return categoryService.registerCategory(categoryRegisterForm, member);
    }

    @DeleteMapping
    public String deleteCategory(@SessionAttribute("member") Member member, @RequestParam String abbreviation) {
        return categoryService.deleteCategory(member, abbreviation);
    }
}
