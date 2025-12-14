package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.TrainingSession;

public interface TrainingSessionDao {
    void save(TrainingSession session);
    TrainingSession findById(Integer id);
    List<TrainingSession> findByTrainer(Integer trainerId);
    List<TrainingSession> findByMember(Integer memberId);
    void delete(Integer id);
<<<<<<< HEAD
}
=======
}

>>>>>>> 91c5274724d7ef4805e337b1d1b1648d6a941856
