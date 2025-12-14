package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.dao.CategoryDao;
import com.secj3303.dao.EnrollmentDao;
import com.secj3303.dao.PersonDao;
import com.secj3303.dao.ProgramDao;
import com.secj3303.model.Category;
import com.secj3303.model.Program;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    // =========================
    // SIMPLE MANUAL CHECK
    // =========================
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    // =========================
    // ADMIN DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("totalMembers", personDao.findByRole("member").size());
        model.addAttribute("totalTrainers", personDao.findByRole("trainer").size());
        model.addAttribute("totalPrograms", programDao.findAll().size());
        model.addAttribute("totalEnrollments", enrollmentDao.findAll().size());

        return "admin-dashboard";
    }

    // =========================
    // SYSTEM SUMMARY
    // =========================
    @GetMapping("/summary")
    public String summary(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("totalMembers", personDao.findByRole("member").size());
        model.addAttribute("totalTrainers", personDao.findByRole("trainer").size());
        model.addAttribute("totalPrograms", programDao.findAll().size());
        model.addAttribute("totalCategories", categoryDao.findAll().size());
        model.addAttribute("totalEnrollments", enrollmentDao.findAll().size());

        return "admin-summary";
    }

    // ==================================================
    // ================= CATEGORY =======================
    // ==================================================

    @GetMapping("/category/list")
    public String listCategories(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("categories", categoryDao.findAll());
        return "category-list";
    }

    @GetMapping("/category/create")
    public String createCategoryForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("category", new Category());
        return "category-form";
    }

    @PostMapping("/category/save")
    public String saveCategory(
            @ModelAttribute("category") Category category,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        categoryDao.save(category);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/category/edit/{id}")
    public String editCategory(
            @PathVariable Integer id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("category", categoryDao.findById(id));
        return "category-form";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(
            @PathVariable Integer id,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        categoryDao.delete(id);
        return "redirect:/admin/category/list";
    }

    // ==================================================
    // ================= PROGRAM ========================
    // ==================================================

    @GetMapping("/program/list")
    public String listPrograms(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("programList", programDao.findAll());
        return "program-list";
    }

    @GetMapping("/program/create")
    public String createProgramForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("program", new Program());
        model.addAttribute("categories", categoryDao.findAll());
        return "program-form";
    }

    @PostMapping("/program/save")
    public String saveProgram(
            @ModelAttribute("program") Program program,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        programDao.save(program);
        return "redirect:/admin/program/list";
    }

    @GetMapping("/program/edit/{id}")
    public String editProgram(
            @PathVariable Integer id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("program", programDao.findById(id));
        model.addAttribute("categories", categoryDao.findAll());
        return "program-form";
    }

    @GetMapping("/program/delete/{id}")
    public String deleteProgram(
            @PathVariable Integer id,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        programDao.delete(id);
        return "redirect:/admin/program/list";
    }
}
