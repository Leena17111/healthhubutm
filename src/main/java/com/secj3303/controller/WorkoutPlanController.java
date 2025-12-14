package com.secj3303.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.ProgramDao;
import com.secj3303.dao.WorkoutPlanDao;
import com.secj3303.model.Program;
import com.secj3303.model.WorkoutPlan;

@Controller
@RequestMapping("/trainer/workout-plan")
public class WorkoutPlanController {

    @Autowired
    private WorkoutPlanDao workoutPlanDao;

    @Autowired
    private ProgramDao programDao;

    // 1️⃣ Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {

        // basic protection (no spring security)
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        List<Program> programs = programDao.findAll();
        model.addAttribute("programs", programs);

        return "trainer-workoutplan-form";
    }

    // 2️⃣ Save workout plan
    @PostMapping("/save")
    public String saveWorkoutPlan(
            @RequestParam Integer programId,
            @RequestParam Integer weekNumber,
            @RequestParam String description) {

        Program program = programDao.findById(programId);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setProgram(program);
        plan.setWeekNumber(weekNumber);
        plan.setDescription(description);

        workoutPlanDao.save(plan);

        return "redirect:/trainer/dashboard";
    }
}
