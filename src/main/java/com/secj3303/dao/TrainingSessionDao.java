package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.TrainingSession;

public interface TrainingSessionDao {
    void save(TrainingSession session);
    TrainingSession findById(Integer id);
    List<TrainingSession> findByTrainer(Integer trainerId);
    List<TrainingSession> findByMember(Integer memberId);
    void delete(Integer id);
}