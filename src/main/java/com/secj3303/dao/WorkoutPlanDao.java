package com.secj3303.dao;

import java.util.List;
import com.secj3303.model.WorkoutPlan;

public interface WorkoutPlanDao {

    void save(WorkoutPlan workoutPlan);

    WorkoutPlan findById(Integer id);

    List<WorkoutPlan> findByProgram(Integer programId);
}