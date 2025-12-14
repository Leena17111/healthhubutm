package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.TrainingSession;

@Repository
public class TrainingSessionDaoHibernate implements TrainingSessionDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    // =========================
    // SAVE OR UPDATE SESSION
    // =========================
    @Override
    public void save(TrainingSession trainingSession) {
        Session session = openSession();
        session.beginTransaction();

        // SAFEST Hibernate operation
        session.saveOrUpdate(trainingSession);

        session.getTransaction().commit();
        session.close();
    }

    // =========================
    // FIND BY ID
    // =========================
    @Override
    public TrainingSession findById(Integer id) {
        Session session = openSession();

        TrainingSession ts = session.get(TrainingSession.class, id);

        session.close();
        return ts;
    }

    // =========================
    // FIND BY TRAINER
    // =========================
    @Override
    public List<TrainingSession> findByTrainer(Integer trainerId) {
        Session session = openSession();

        List<TrainingSession> list = session.createQuery(
                "FROM TrainingSession ts " +
                "LEFT JOIN FETCH ts.trainer " +
                "LEFT JOIN FETCH ts.member " +
                "WHERE ts.trainer.id = :trainerId " +
                "ORDER BY ts.sessionDate DESC, ts.sessionTime DESC",
                TrainingSession.class
        )
        .setParameter("trainerId", trainerId)
        .list();

        session.close();
        return list;
    }

    // =========================
    // FIND BY MEMBER
    // =========================
    @Override
    public List<TrainingSession> findByMember(Integer memberId) {
        Session session = openSession();

        List<TrainingSession> list = session.createQuery(
                "FROM TrainingSession ts " +
                "WHERE ts.member.id = :memberId " +
                "ORDER BY ts.sessionDate DESC, ts.sessionTime DESC",
                TrainingSession.class
        )
        .setParameter("memberId", memberId)
        .list();

        session.close();
        return list;
    }

    // =========================
    // DELETE SESSION
    // =========================
    @Override
    public void delete(Integer id) {
        Session session = openSession();
        session.beginTransaction();

        TrainingSession ts = session.get(TrainingSession.class, id);
        if (ts != null) {
            session.delete(ts);
        }

        session.getTransaction().commit();
        session.close();
    }
}
