package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.secj3303.dao.PersonDao;
import com.secj3303.model.Person;

@Controller
public class AuthController {

    @Autowired
    private PersonDao personDao;

    // =========================
    // SHOW LOGIN PAGE
    // =========================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // =========================
    // PROCESS LOGIN
    // =========================
    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Person person = personDao.findByEmailAndPassword(email, password);

        if (person == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        // save user in session
        session.setAttribute("loggedUser", person);
        session.setAttribute("role", person.getRole());

        // redirect based on role
        if (person.getRole().equals("member")) {
            return "redirect:/bmi/form";
        } else if (person.getRole().equals("admin")) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/trainer/dashboard";
        }
    }

    // =========================
    // SHOW REGISTER PAGE (MEMBER ONLY)
    // =========================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("person", new Person());
        return "register";
    }

    // =========================
    // PROCESS REGISTER
    // =========================
    @PostMapping("/register")
    public String doRegister(@ModelAttribute Person person) {

        // force role = member
        person.setRole("member");

        // save member
        personDao.save(person);

        return "redirect:/login";
    }

    // =========================
    // LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
