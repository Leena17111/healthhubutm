package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.WorkoutMemberProgress;

public interface WorkoutMemberProgressDao {

    void save(WorkoutMemberProgress progress);

    WorkoutMemberProgress findById(Integer id);   // ✅ REQUIRED

    WorkoutMemberProgress findByMemberAndPlan(Integer memberId, Integer workoutPlanId);

    List<WorkoutMemberProgress> findByWorkoutPlan(Integer workoutPlanId);

    List<WorkoutMemberProgress> findByMember(Integer memberId);

    List<WorkoutMemberProgress> findAll();
}
