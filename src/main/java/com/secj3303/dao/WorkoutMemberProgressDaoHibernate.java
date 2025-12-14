package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.WorkoutMemberProgress;

@Repository
public class WorkoutMemberProgressDaoHibernate
        implements WorkoutMemberProgressDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void save(WorkoutMemberProgress progress) {
        Session session = openSession();
        session.beginTransaction();
        session.saveOrUpdate(progress);
        session.getTransaction().commit();
        session.close();
    }

    // ✅ THIS WAS MISSING
    @Override
    public WorkoutMemberProgress findById(Integer id) {
        Session session = openSession();
        WorkoutMemberProgress progress =
                session.get(WorkoutMemberProgress.class, id);
        session.close();
        return progress;
    }

    @Override
    public WorkoutMemberProgress findByMemberAndPlan(Integer memberId, Integer workoutPlanId) {
        Session session = openSession();

        WorkoutMemberProgress result = session.createQuery(
                "FROM WorkoutMemberProgress p " +
                "WHERE p.member.id = :mid AND p.workoutPlan.id = :wid",
                WorkoutMemberProgress.class
        )
        .setParameter("mid", memberId)
        .setParameter("wid", workoutPlanId)
        .uniqueResult();

        session.close();
        return result;
    }

    @Override
    public List<WorkoutMemberProgress> findByWorkoutPlan(Integer workoutPlanId) {
        Session session = openSession();

        List<WorkoutMemberProgress> list = session.createQuery(
                "FROM WorkoutMemberProgress p WHERE p.workoutPlan.id = :wid",
                WorkoutMemberProgress.class
        )
        .setParameter("wid", workoutPlanId)
        .list();

        session.close();
        return list;
    }

    @Override
    public List<WorkoutMemberProgress> findByMember(Integer memberId) {
        Session session = openSession();

        List<WorkoutMemberProgress> list = session.createQuery(
                "FROM WorkoutMemberProgress p WHERE p.member.id = :mid",
                WorkoutMemberProgress.class
        )
        .setParameter("mid", memberId)
        .list();

        session.close();
        return list;
    }

    @Override
    public List<WorkoutMemberProgress> findAll() {
        Session session = openSession();
        List<WorkoutMemberProgress> list = session.createQuery(
                "FROM WorkoutMemberProgress",
                WorkoutMemberProgress.class
        ).list();
        session.close();
        return list;
    }

}
