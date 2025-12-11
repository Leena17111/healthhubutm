package com.secj3303.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.secj3303.dao.ProgramDao;
import com.secj3303.model.Program;

@Controller
@RequestMapping("/program")
public class ProgramController {

    @Autowired
    private ProgramDao programDao;

    // ===================================
    // 1. LIST ALL PROGRAMS
    // URL: /program/list
    // ===================================
    @GetMapping("/list")
    public String listPrograms(Model model) {
        model.addAttribute("programList", programDao.findAll());
        return "program-list";  // Thymeleaf view
    }

    // ===================================
    // 2. SHOW FORM TO CREATE NEW PROGRAM
    // URL: /program/create
    // ===================================
    @GetMapping("/create")
    public String createProgramForm(Model model) {
        model.addAttribute("program", new Program());
        return "program-form";
    }

    // ===================================
    // 3. SAVE PROGRAM (INSERT OR UPDATE)
    // URL: /program/save
    // ===================================
    @PostMapping("/save")
    public String saveProgram(@ModelAttribute("program") Program program) {
        programDao.save(program);  // DAO handles insert/update automatically
        return "redirect:/program/list";
    }

    // ===================================
    // 4. EDIT PROGRAM (LOAD BY ID)
    // URL: /program/edit/{id}
    // ===================================
    @GetMapping("/edit/{id}")
    public String editProgram(@PathVariable("id") Integer id, Model model) {
        Program program = programDao.findById(id);
        model.addAttribute("program", program);
        return "program-form";
    }

    // ===================================
    // 5. DELETE PROGRAM
    // URL: /program/delete/{id}
    // ===================================
    @GetMapping("/delete/{id}")
    public String deleteProgram(@PathVariable("id") Integer id) {
        programDao.delete(id);
        return "redirect:/program/list";
    }
}
