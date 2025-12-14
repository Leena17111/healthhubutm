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

    @Override
    public void save(TrainingSession session) {
        Session hibernateSession = openSession();
        hibernateSession.beginTransaction();
        
        if (session.getId() == null) {
            hibernateSession.save(session);
        } else {
            hibernateSession.update(session);
        }
        
        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

    @Override
    public TrainingSession findById(Integer id) {
        Session hibernateSession = openSession();
        TrainingSession session = hibernateSession.get(TrainingSession.class, id);
        hibernateSession.close();
        return session;
    }

    @Override
    public List<TrainingSession> findByTrainer(Integer trainerId) {
        Session hibernateSession = openSession();
        
        List<TrainingSession> list = hibernateSession.createQuery(
            "FROM TrainingSession WHERE trainer.id = :trainerId ORDER BY sessionDate DESC, sessionTime DESC",
            TrainingSession.class
        )
        .setParameter("trainerId", trainerId)
        .list();
        
        hibernateSession.close();
        return list;
    }

    @Override
    public List<TrainingSession> findByMember(Integer memberId) {
        Session hibernateSession = openSession();
        
        List<TrainingSession> list = hibernateSession.createQuery(
            "FROM TrainingSession WHERE member.id = :memberId ORDER BY sessionDate DESC, sessionTime DESC",
            TrainingSession.class
        )
        .setParameter("memberId", memberId)
        .list();
        
        hibernateSession.close();
        return list;
    }

    @Override
    public void delete(Integer id) {
        Session hibernateSession = openSession();
        hibernateSession.beginTransaction();
        
        TrainingSession session = hibernateSession.get(TrainingSession.class, id);
        if (session != null) {
            hibernateSession.delete(session);
        }
        
        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }
<<<<<<< HEAD
}
=======
}

>>>>>>> 91c5274724d7ef4805e337b1d1b1648d6a941856
