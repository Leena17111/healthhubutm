package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.dao.CategoryDao;
import com.secj3303.dao.EnrollmentDao;
import com.secj3303.dao.PersonDao;
import com.secj3303.dao.ProgramDao;

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
    // ADMIN DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        if (session.getAttribute("loggedUser") == null) {
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

        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        model.addAttribute("totalMembers", personDao.findByRole("member").size());
        model.addAttribute("totalTrainers", personDao.findByRole("trainer").size());
        model.addAttribute("totalPrograms", programDao.findAll().size());
        model.addAttribute("totalCategories", categoryDao.findAll().size());
        model.addAttribute("totalEnrollments", enrollmentDao.findAll().size());

        return "admin-summary";
    }
}
