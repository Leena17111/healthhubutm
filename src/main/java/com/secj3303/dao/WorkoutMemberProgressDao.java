package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.WorkoutMemberProgress;

public interface WorkoutMemberProgressDao {

    void save(WorkoutMemberProgress progress);

    WorkoutMemberProgress findByMemberAndPlan(Integer memberId, Integer workoutPlanId);

    List<WorkoutMemberProgress> findByWorkoutPlan(Integer workoutPlanId);

    List<WorkoutMemberProgress> findByMember(Integer memberId);
}
