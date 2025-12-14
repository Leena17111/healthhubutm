package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Enrollment;

@Repository
public class EnrollmentDaoHibernate implements EnrollmentDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    // =========================
    // SAVE OR UPDATE ENROLLMENT
    // =========================
    @Override
    public void save(Enrollment enrollment) {
        Session session = openSession();
        session.beginTransaction();

        session.saveOrUpdate(enrollment); // 🔑 IMPORTANT

        session.getTransaction().commit();
        session.close();
    }

    // =========================
    // FIND ENROLLMENTS BY MEMBER
    // =========================
    @Override
    public List<Enrollment> findByMember(Integer memberId) {
        Session session = openSession();

        List<Enrollment> list = session.createQuery(
                "FROM Enrollment e WHERE e.member.id = :mid",
                Enrollment.class
        )
        .setParameter("mid", memberId)
        .list();

        session.close();
        return list;
    }

    // =========================
    // FIND BY MEMBER + PROGRAM
    // =========================
    @Override
    public Enrollment findByMemberAndProgram(Integer memberId, Integer programId) {
        Session session = openSession();

        Enrollment enrollment = session.createQuery(
                "FROM Enrollment e WHERE e.member.id = :mid AND e.program.id = :pid",
                Enrollment.class
        )
        .setParameter("mid", memberId)
        .setParameter("pid", programId)
        .uniqueResult();

        session.close();
        return enrollment;
    }

    // =========================
    // FIND ALL
    // =========================
    @Override
    public List<Enrollment> findAll() {
        Session session = openSession();

        List<Enrollment> list = session.createQuery(
                "FROM Enrollment",
                Enrollment.class
        ).list();

        session.close();
        return list;
    }
}
