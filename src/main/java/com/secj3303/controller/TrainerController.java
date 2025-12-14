package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.dao.WorkoutMemberProgressDao;
import com.secj3303.model.Person;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private WorkoutMemberProgressDao workoutMemberProgressDao;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "trainer-dashboard";
    }

    // ✅ FIXED HERE
    @GetMapping("/monitor-workouts")
    public String monitorWorkouts(HttpSession session, Model model) {

        Person trainer = (Person) session.getAttribute("loggedUser");
        if (trainer == null || !"trainer".equals(trainer.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("progressList",
                workoutMemberProgressDao.findAll());

        return "trainer-monitor-workouts";
    }
}
