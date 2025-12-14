package com.secj3303.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.EnrollmentDao;
import com.secj3303.dao.WorkoutMemberProgressDao;
import com.secj3303.dao.WorkoutPlanDao;
import com.secj3303.model.Enrollment;
import com.secj3303.model.Person;
import com.secj3303.model.WorkoutMemberProgress;
import com.secj3303.model.WorkoutPlan;

@Controller
@RequestMapping("/member/workouts")
public class MemberWorkoutController {

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private WorkoutPlanDao workoutPlanDao;

    @Autowired
    private WorkoutMemberProgressDao progressDao;

    // ================================
    // 1️⃣ View member workout plans
    // ================================
    @GetMapping
    public String viewWorkouts(HttpSession session, Model model) {

        Person member = (Person) session.getAttribute("loggedUser");

        if (member == null || !"member".equals(member.getRole())) {
            return "redirect:/login";
        }

        // Get enrolled programs
        List<Enrollment> enrollments =
                enrollmentDao.findByMember(member.getId());

        // Keep order (week number)
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

                // Create progress record if not exists
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

    // ================================
    // 2️⃣ Mark workout as completed
    // ================================
    @PostMapping("/complete")
    public String completeWorkout(@RequestParam Integer progressId) {

        WorkoutMemberProgress progress =
                progressDao.findById(progressId);

        if (progress != null) {
            progress.setCompleted(true);
            progressDao.save(progress);
        }

        
        return "redirect:/member/workouts";
    }
}
