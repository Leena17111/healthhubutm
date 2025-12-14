package com.secj3303.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.PersonDao;
import com.secj3303.dao.ProgramDao;
import com.secj3303.dao.TrainingSessionDao;
import com.secj3303.dao.WorkoutMemberProgressDao;
import com.secj3303.dao.WorkoutPlanDao;
import com.secj3303.model.Person;
import com.secj3303.model.Program;
import com.secj3303.model.TrainingSession;
import com.secj3303.model.WorkoutPlan;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainingSessionDao trainingSessionDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private WorkoutMemberProgressDao workoutMemberProgressDao;

    @Autowired
    private WorkoutPlanDao workoutPlanDao;

    @Autowired
    private ProgramDao programDao;

    // =========================
    // TRAINER CHECK
    // =========================
    private Person getTrainer(HttpSession session) {
        Person user = (Person) session.getAttribute("loggedUser");
        if (user == null || !"trainer".equals(user.getRole())) {
            return null;
        }
        return user;
    }

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (getTrainer(session) == null) {
            return "redirect:/login";
        }
        return "trainer-dashboard";
    }

    // =========================
    // CREATE TRAINING SESSION
    // =========================
    @GetMapping("/sessions/create")
    public String showScheduleForm(Model model, HttpSession session) {
        if (getTrainer(session) == null) {
            return "redirect:/login";
        }

        model.addAttribute("members", personDao.findByRole("member"));
        return "trainer-session-form";
    }

    @PostMapping("/sessions/save")
    public String saveSession(
            @RequestParam Integer memberId,
            @RequestParam String sessionDate,
            @RequestParam String sessionTime,
            @RequestParam(required = false) String notes,
            HttpSession session) {

        Person trainer = getTrainer(session);
        if (trainer == null) {
            return "redirect:/login";
        }

        Person member = personDao.findById(memberId);

        TrainingSession ts = new TrainingSession();
        ts.setTrainer(trainer);
        ts.setMember(member);
        ts.setSessionDate(LocalDate.parse(sessionDate));
        ts.setSessionTime(LocalTime.parse(sessionTime));
        ts.setNotes(notes);

        trainingSessionDao.save(ts);

        return "redirect:/trainer/sessions/list";
    }

    @GetMapping("/sessions/list")
    public String listSessions(Model model, HttpSession session) {
        Person trainer = getTrainer(session);
        if (trainer == null) {
            return "redirect:/login";
        }

        model.addAttribute(
            "sessions",
            trainingSessionDao.findByTrainer(trainer.getId())
        );

        return "trainer-sessions-list";
    }

    // =========================
    // ONE PLAN PER WEEK (MANUAL DESC)
    // =========================
    @GetMapping("/workout-plan/create")
    public String showWorkoutPlanForm(
            @RequestParam(required = false) Integer programId,
            Model model,
            HttpSession session) {

        if (getTrainer(session) == null) {
            return "redirect:/login";
        }

        model.addAttribute("programs", programDao.findAll());

        if (programId != null) {
            Program program = programDao.findById(programId);
            model.addAttribute("selectedProgram", program);
            model.addAttribute("totalWeeks", program.getDurationWeeks());
        }

        return "trainer-workoutplan-form";
    }

    @PostMapping("/workout-plan/save")
    public String saveWorkoutPlans(
            @RequestParam Integer programId,
            @RequestParam("descriptions") String[] descriptions,
            HttpSession session) {

        if (getTrainer(session) == null) {
            return "redirect:/login";
        }

        Program program = programDao.findById(programId);

        for (int i = 0; i < descriptions.length; i++) {
            WorkoutPlan plan = new WorkoutPlan();
            plan.setProgram(program);
            plan.setWeekNumber(i + 1);
            plan.setDescription(descriptions[i]);

            workoutPlanDao.save(plan);
        }

        return "redirect:/trainer/dashboard";
    }

    // =========================
    // MONITOR WORKOUTS
    // =========================
    @GetMapping("/monitor-workouts")
    public String monitorWorkouts(HttpSession session, Model model) {

        Person trainer = getTrainer(session);
        if (trainer == null) {
            return "redirect:/login";
        }

        model.addAttribute(
            "progressList",
            workoutMemberProgressDao.findAll()
        );

        return "trainer-monitor-workouts";
    }
}
