package com.secj3303.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.PersonDao;
import com.secj3303.dao.TrainingSessionDao;
import com.secj3303.dao.WorkoutMemberProgressDao;
import com.secj3303.model.Person;
import com.secj3303.model.TrainingSession;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainingSessionDao trainingSessionDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private WorkoutMemberProgressDao workoutMemberProgressDao;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        return "trainer-dashboard";
    }

    // =========================
    // SHOW SCHEDULE SESSION FORM
    // =========================
    @GetMapping("/sessions/create")
    public String showScheduleForm(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        // Get all members for the dropdown
        List<Person> members = personDao.findByRole("member");
        model.addAttribute("members", members);

        return "trainer-session-form";
    }

    // =========================
    // SAVE SCHEDULED SESSION
    // =========================
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

    // =========================
    // LIST ALL SCHEDULED SESSIONS
    // =========================
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

    // =========================
    // DELETE SESSION
    // =========================
    @GetMapping("/sessions/delete")
    public String deleteSession(@RequestParam Integer id, HttpSession httpSession) {
        if (httpSession.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        trainingSessionDao.delete(id);
        return "redirect:/trainer/sessions/list";
    }

    // MONITOR WORKOUTS
    @GetMapping("/monitor-workouts")
    public String monitorWorkouts(HttpSession session, Model model) {

        Person trainer = (Person) session.getAttribute("loggedUser");
        if (trainer == null || !"trainer".equals(trainer.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("progressList",
                workoutMemberProgressDao.findAll());

        return "trainer-monitor-workouts"; }
}