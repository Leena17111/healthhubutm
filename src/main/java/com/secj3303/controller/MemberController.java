package com.secj3303.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.BmiDao;
import com.secj3303.dao.EnrollmentDao;
import com.secj3303.dao.ProgramDao;
import com.secj3303.dao.WorkoutMemberProgressDao;
import com.secj3303.dao.WorkoutPlanDao;
import com.secj3303.model.BmiRecord;
import com.secj3303.model.Enrollment;
import com.secj3303.model.Person;
import com.secj3303.model.Program;
import com.secj3303.model.WorkoutMemberProgress;
import com.secj3303.model.WorkoutPlan;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private BmiDao bmiDao;
    
    @Autowired
    private ProgramDao programDao;
    
    @Autowired
    private EnrollmentDao enrollmentDao;
    
    @Autowired
    private WorkoutPlanDao workoutPlanDao;
    
    @Autowired
    private WorkoutMemberProgressDao workoutMemberProgressDao;

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        return "member-dashboard";
    }

    // =========================
    // BMI FUNCTIONALITIES
    // =========================
    
    @GetMapping("/bmi/form")
    public String showBmiForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        return "bmi-form";
    }

    @PostMapping("/bmi/calc")
    public String calcBmi(
            @RequestParam double weight,
            @RequestParam double height,
            HttpSession session,
            Model model) {

        Person person = (Person) session.getAttribute("loggedUser");
        if (person == null) {
            return "redirect:/login";
        }

        double bmi = weight / (height * height);
        String category;

        if (bmi < 18.5) category = "Underweight";
        else if (bmi <= 24.9) category = "Normal";
        else if (bmi <= 29.9) category = "Overweight";
        else category = "Obese";

        BmiRecord record = new BmiRecord();
        record.setPerson(person);
        record.setHeight(height);
        record.setWeight(weight);
        record.setBmiValue(bmi);
        record.setCategory(category);

        bmiDao.save(record);

        model.addAttribute("bmi", String.format("%.2f", bmi));
        model.addAttribute("status", category);

        return "bmi-result";
    }

    @GetMapping("/bmi/history")
    public String viewBmiHistory(HttpSession session, Model model) {
        Person person = (Person) session.getAttribute("loggedUser");
        if (person == null) {
            return "redirect:/login";
        }

        model.addAttribute("records", bmiDao.findByPerson(person.getId()));
        return "bmi-history";
    }

    // =========================
    // PROGRAM FUNCTIONALITIES
    // =========================
    
    @GetMapping("/programs")
    public String browsePrograms(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        model.addAttribute("programs", programDao.findAll());
        return "member-programs";
    }

    @GetMapping("/enroll/{programId}")
    public String enroll(
            @PathVariable Integer programId,
            HttpSession session) {

        Person member = (Person) session.getAttribute("loggedUser");
        if (member == null) {
            return "redirect:/login";
        }

        Program program = programDao.findById(programId);

        Enrollment enrollment = new Enrollment();
        enrollment.setMember(member);
        enrollment.setProgram(program);
        enrollment.setEnrollmentDate(LocalDate.now());

        enrollmentDao.save(enrollment);

        return "redirect:/member/my-programs";
    }

    @GetMapping("/my-programs")
    public String myPrograms(Model model, HttpSession session) {
        Person member = (Person) session.getAttribute("loggedUser");
        if (member == null) {
            return "redirect:/login";
        }

        model.addAttribute("enrollments", enrollmentDao.findByMember(member.getId()));
        return "my-programs";
    }

    // =========================
    // PERSONAL WORKOUT PLAN
    // =========================
    
    @GetMapping("/workout-plans")
    public String myWorkoutPlans(HttpSession session, Model model) {
        Person member = (Person) session.getAttribute("loggedUser");
        if (member == null) {
            return "redirect:/login";
        }

        // Get all enrollments for this member
        List<Enrollment> enrollments = enrollmentDao.findByMember(member.getId());
        
        // For each enrollment, get workout plans for that program
        List<Map<String, Object>> plansWithProgress = new ArrayList<>();
        
        for (Enrollment enrollment : enrollments) {
            List<WorkoutPlan> plans = workoutPlanDao.findByProgram(enrollment.getProgram().getId());
            
            for (WorkoutPlan plan : plans) {
                Map<String, Object> planData = new HashMap<>();
                planData.put("plan", plan);
                planData.put("program", enrollment.getProgram());
                
                // Check if member has progress for this plan
                WorkoutMemberProgress progress = 
                    workoutMemberProgressDao.findByMemberAndPlan(member.getId(), plan.getId());
                planData.put("progress", progress);
                
                plansWithProgress.add(planData);
            }
        }
        
        model.addAttribute("plansWithProgress", plansWithProgress);
        return "member-workout-plans";
    }

    @PostMapping("/workout-plans/complete")
    public String completeWorkoutPlan(
            @RequestParam Integer workoutPlanId,
            HttpSession session) {
        
        Person member = (Person) session.getAttribute("loggedUser");
        if (member == null) {
            return "redirect:/login";
        }

        WorkoutPlan plan = workoutPlanDao.findById(workoutPlanId);
        
        WorkoutMemberProgress progress = 
            workoutMemberProgressDao.findByMemberAndPlan(member.getId(), workoutPlanId);
        
        if (progress == null) {
            progress = new WorkoutMemberProgress();
            progress.setMember(member);
            progress.setWorkoutPlan(plan);
        }
        
        progress.setCompleted(true);
        workoutMemberProgressDao.save(progress);
        
        return "redirect:/member/workout-plans";
    }
}
