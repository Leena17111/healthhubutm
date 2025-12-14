package com.secj3303.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
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
    private ProgramDao programDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private BmiDao bmiDao;

    @Autowired
    private WorkoutPlanDao workoutPlanDao;

    @Autowired
    private WorkoutMemberProgressDao progressDao;

    // =========================
    // GET LOGGED MEMBER
    // =========================
    private Person getMember(HttpSession session) {
        return (Person) session.getAttribute("loggedUser");
    }

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (getMember(session) == null) {
            return "redirect:/login";
        }
        return "member-dashboard";
    }

    // =========================
    // BROWSE PROGRAMS
    // =========================
    @GetMapping("/programs")
    public String browsePrograms(Model model, HttpSession session) {

        Person member = getMember(session);
        if (member == null) {
            return "redirect:/login";
        }

        model.addAttribute("programs", programDao.findAll());

        List<Integer> enrolledProgramIds =
                enrollmentDao.findByMember(member.getId())
                             .stream()
                             .map(e -> e.getProgram().getId())
                             .toList();

        model.addAttribute("enrolledProgramIds", enrolledProgramIds);

        return "member-programs";
    }

    // =========================
    // ENROLL PROGRAM
    // =========================
    @GetMapping("/enroll/{programId}")
    public String enroll(@PathVariable Integer programId, HttpSession session) {

        Person member = getMember(session);
        if (member == null) {
            return "redirect:/login";
        }

        Enrollment existing =
                enrollmentDao.findByMemberAndProgram(member.getId(), programId);

        if (existing == null) {
            Program program = programDao.findById(programId);

            Enrollment enrollment = new Enrollment();
            enrollment.setMember(member);
            enrollment.setProgram(program);
            enrollment.setEnrollmentDate(LocalDate.now());

            enrollmentDao.save(enrollment);
        }

        return "redirect:/member/programs";
    }

    // =========================
    // MY PROGRAMS
    // =========================
    @GetMapping("/my-programs")
    public String myPrograms(Model model, HttpSession session) {

        Person member = getMember(session);
        if (member == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "enrollments",
                enrollmentDao.findByMember(member.getId())
        );

        return "my-programs";
    }

    // =========================
    // WORKOUT PLANS
    // =========================
    @GetMapping("/workouts")
    public String viewWorkouts(HttpSession session, Model model) {

        Person member = getMember(session);
        if (member == null) {
            return "redirect:/login";
        }

        List<Enrollment> enrollments =
                enrollmentDao.findByMember(member.getId());

        Map<WorkoutPlan, WorkoutMemberProgress> workouts =
                new LinkedHashMap<>();

        for (Enrollment enrollment : enrollments) {

            List<WorkoutPlan> plans =
                    workoutPlanDao.findByProgram(
                            enrollment.getProgram().getId());

            for (WorkoutPlan plan : plans) {

                WorkoutMemberProgress progress =
                        progressDao.findByMemberAndPlan(
                                member.getId(), plan.getId());

                if (progress == null) {
                    progress = new WorkoutMemberProgress();
                    progress.setMember(member);
                    progress.setWorkoutPlan(plan);
                    progress.setCompleted(false);
                    progressDao.save(progress);
                }

                workouts.put(plan, progress);
            }
        }

        model.addAttribute("workouts", workouts);
        return "member-workout-list";
    }

    // =========================
    // COMPLETE WORKOUT
    // =========================
    @PostMapping("/workouts/complete")
    public String completeWorkout(@RequestParam Integer progressId) {

        WorkoutMemberProgress progress =
                progressDao.findById(progressId);

        if (progress != null) {
            progress.setCompleted(true);
            progressDao.save(progress);
        }

        return "redirect:/member/workouts";
    }

    // ==================================================
    // ===================== BMI =========================
    // ==================================================

    // SHOW BMI FORM
    @GetMapping("/bmi/form")
    public String showBmiForm(HttpSession session) {

        Person member = getMember(session);
        if (member == null) {
            return "redirect:/login";
        }

        return "bmi-form";
    }

    // CALCULATE + SAVE BMI
    @PostMapping("/bmi/calc")
    public String calcBmi(
            @RequestParam double weight,
            @RequestParam double height,
            HttpSession session,
            Model model) {

        Person member = getMember(session);
        if (member == null) {
            return "redirect:/login";
        }

        double bmi = weight / (height * height);
        String category;

        if (bmi < 18.5) category = "Underweight";
        else if (bmi <= 24.9) category = "Normal";
        else if (bmi <= 29.9) category = "Overweight";
        else category = "Obese";

        BmiRecord record = new BmiRecord();
        record.setPerson(member);
        record.setHeight(height);
        record.setWeight(weight);
        record.setBmiValue(bmi);
        record.setCategory(category);

        bmiDao.save(record);

        model.addAttribute("bmi", String.format("%.2f", bmi));
        model.addAttribute("status", category);

        return "bmi-result";
    }

    // BMI HISTORY
    @GetMapping("/bmi/history")
    public String bmiHistory(HttpSession session, Model model) {

        Person member = getMember(session);
        if (member == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "records",
                bmiDao.findByPerson(member.getId())
        );

        return "bmi-history";
    }
}
