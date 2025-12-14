package com.secj3303.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.dao.CategoryDao;
import com.secj3303.model.Category;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    // ================================
    // LIST ALL CATEGORIES
    // ================================
    @GetMapping("/list")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryDao.findAll());
        return "category-list";
    }

    // ================================
    // SHOW CREATE FORM
    // ================================
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    // ================================
    // SAVE CATEGORY (Create + Update)
    // ================================
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryDao.save(category);
        return "redirect:/admin/category/list";
    }

    // ================================
    // EDIT CATEGORY
    // ================================
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable Integer id, Model model) {
        Category category = categoryDao.findById(id);
        model.addAttribute("category", category);
        return "category-form";
    }

    // ================================
    // DELETE CATEGORY
    // ================================
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        categoryDao.delete(id);
        return "redirect:/admin/category/list";
    }
}
