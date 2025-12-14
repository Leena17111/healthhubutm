package com.secj3303.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.secj3303.dao.PersonDao;
import com.secj3303.dao.ProgramDao;
import com.secj3303.dao.TrainingSessionDao;
import com.secj3303.dao.WorkoutPlanDao;
import com.secj3303.dao.WorkoutMemberProgressDao;
import com.secj3303.dao.EnrollmentDao;
import com.secj3303.model.Person;
import com.secj3303.model.Program;
import com.secj3303.model.TrainingSession;
import com.secj3303.model.WorkoutPlan;
import com.secj3303.model.WorkoutMemberProgress;
import com.secj3303.model.Enrollment;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainingSessionDao trainingSessionDao;

    @Autowired
    private PersonDao personDao;
    
    @Autowired
    private ProgramDao programDao;
    
    @Autowired
    private WorkoutPlanDao workoutPlanDao;
    
    @Autowired
    private WorkoutMemberProgressDao workoutMemberProgressDao;
    
    @Autowired
    private EnrollmentDao enrollmentDao;

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        return "trainer-dashboard";
    }

    // =========================
    // FITNESS PLAN (Workout Plan) FUNCTIONALITIES
    // =========================
    
    @GetMapping("/fitness-plans/create")
    public String showCreateFitnessPlanForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        List<Program> programs = programDao.findAll();
        model.addAttribute("programs", programs);
        model.addAttribute("workoutPlan", new WorkoutPlan());

        return "trainer-workoutplan-form";
    }

    @PostMapping("/fitness-plans/save")
    public String saveFitnessPlan(
            @RequestParam Integer programId,
            @RequestParam Integer weekNumber,
            @RequestParam String description,
            HttpSession session) {

        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        Program program = programDao.findById(programId);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setProgram(program);
        plan.setWeekNumber(weekNumber);
        plan.setDescription(description);

        workoutPlanDao.save(plan);

        return "redirect:/trainer/fitness-plans/list";
    }

    @GetMapping("/fitness-plans/list")
    public String listFitnessPlans(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        List<WorkoutPlan> plans = workoutPlanDao.findAll(); // You may need to add findAll() to WorkoutPlanDao
        model.addAttribute("plans", plans);
        return "trainer-fitness-plans-list";
    }

    // =========================
    // ASSIGN PLAN TO MEMBERS
    // =========================
    
    @GetMapping("/fitness-plans/assign/{planId}")
    public String showAssignPlanForm(@PathVariable Integer planId, Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        WorkoutPlan plan = workoutPlanDao.findById(planId);
        List<Person> members = personDao.findByRole("member");
        
        // Filter members enrolled in the program
        List<Enrollment> enrollments = enrollmentDao.findByProgram(plan.getProgram().getId());
        List<Person> enrolledMembers = new java.util.ArrayList<>();
        for (Enrollment e : enrollments) {
            enrolledMembers.add(e.getMember());
        }
        
        model.addAttribute("plan", plan);
        model.addAttribute("members", enrolledMembers);
        return "trainer-assign-plan";
    }

    @PostMapping("/fitness-plans/assign")
    public String assignPlanToMember(
            @RequestParam Integer planId,
            @RequestParam Integer memberId,
            HttpSession session) {

        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        WorkoutPlan plan = workoutPlanDao.findById(planId);
        Person member = personDao.findById(memberId);

        // Create or update progress record
        WorkoutMemberProgress progress = workoutMemberProgressDao.findByMemberAndPlan(memberId, planId);
        if (progress == null) {
            progress = new WorkoutMemberProgress();
            progress.setMember(member);
            progress.setWorkoutPlan(plan);
            progress.setCompleted(false);
        }

        workoutMemberProgressDao.save(progress);

        return "redirect:/trainer/fitness-plans/list";
    }

    // =========================
    // MONITOR MEMBER PROGRESS
    // =========================
    
    @GetMapping("/members/progress")
    public String monitorMemberProgress(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        List<Person> members = personDao.findByRole("member");
        model.addAttribute("members", members);
        return "trainer-member-progress-list";
    }

    @GetMapping("/members/{memberId}/progress")
    public String viewMemberProgress(@PathVariable Integer memberId, Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        Person member = personDao.findById(memberId);
        List<WorkoutMemberProgress> progressList = workoutMemberProgressDao.findByMember(memberId);

        model.addAttribute("member", member);
        model.addAttribute("progressList", progressList);
        return "trainer-member-progress-detail";
    }

    // =========================
    // SCHEDULE SESSIONS
    // =========================
    
    @GetMapping("/sessions/create")
    public String showScheduleForm(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        List<Person> members = personDao.findByRole("member");
        model.addAttribute("members", members);
        return "trainer-session-form";
    }

    @PostMapping("/sessions/save")
    public String saveSession(
            @RequestParam Integer memberId,
            @RequestParam String sessionDate,
            @RequestParam String sessionTime,
            @RequestParam(required = false) String notes,
            HttpSession httpSession) {

        if (httpSession.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        Person trainer = (Person) httpSession.getAttribute("loggedUser");
        Person member = personDao.findById(memberId);

        TrainingSession session = new TrainingSession();
        session.setTrainer(trainer);
        session.setMember(member);
        session.setSessionDate(LocalDate.parse(sessionDate));
        session.setSessionTime(LocalTime.parse(sessionTime));
        session.setNotes(notes);

        trainingSessionDao.save(session);

        return "redirect:/trainer/sessions/list";
    }

    @GetMapping("/sessions/list")
    public String listSessions(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        Person trainer = (Person) httpSession.getAttribute("loggedUser");
        List<TrainingSession> sessions = trainingSessionDao.findByTrainer(trainer.getId());
        
        model.addAttribute("sessions", sessions);
        return "trainer-sessions-list";
    }

    @GetMapping("/sessions/delete/{id}")
    public String deleteSession(@PathVariable Integer id, HttpSession httpSession) {
        if (httpSession.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        trainingSessionDao.delete(id);
        return "redirect:/trainer/sessions/list";
    }
}
