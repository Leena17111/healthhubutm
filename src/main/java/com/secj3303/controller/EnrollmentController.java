package com.secj3303.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.secj3303.dao.EnrollmentDao;
import com.secj3303.dao.ProgramDao;
import com.secj3303.model.Enrollment;
import com.secj3303.model.Person;
import com.secj3303.model.Program;

@Controller
@RequestMapping("/member")
public class EnrollmentController {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    // =========================
    // 1. Browse Programs (MEMBER)
    // =========================
    @GetMapping("/programs")
    public String browsePrograms(Model model) {
        model.addAttribute("programs", programDao.findAll());
        return "member-programs"; // ✅ FIXED
    }

    // =========================
    // 2. Enroll in Program
    // =========================
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

    // =========================
    // 3. My Programs
    // =========================
    @GetMapping("/my-programs")
    public String myPrograms(Model model, HttpSession session) {

        Person member = (Person) session.getAttribute("loggedUser");
        if (member == null) {
            return "redirect:/login";
        }

        model.addAttribute(
            "enrollments",
            enrollmentDao.findByMember(member.getId())
        );

        return "my-programs"; // ✅ CORRECT
    }
}
