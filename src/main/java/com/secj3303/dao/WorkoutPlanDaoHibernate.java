package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.WorkoutPlan;

@Repository
public class WorkoutPlanDaoHibernate implements WorkoutPlanDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void save(WorkoutPlan workoutPlan) {
        Session session = openSession();
        session.beginTransaction();
        session.save(workoutPlan);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public WorkoutPlan findById(Integer id) {
        Session session = openSession();
        WorkoutPlan plan = session.get(WorkoutPlan.class, id);
        session.close();
        return plan;
    }

    @Override
    public List<WorkoutPlan> findByProgram(Integer programId) {
        Session session = openSession();

        List<WorkoutPlan> list = session.createQuery(
            "FROM WorkoutPlan wp WHERE wp.program.id = :pid ORDER BY wp.weekNumber",
            WorkoutPlan.class
        )
        .setParameter("pid", programId)
        .list();

        session.close();
        return list;
    }
}
