package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.BmiRecord;

@Repository
public class BmiDaoHibernate implements BmiDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    // =========================
    // SAVE BMI RECORD
    // =========================
    @Override
    public void save(BmiRecord record) {
        Session session = openSession();
        session.beginTransaction();
        session.save(record);
        session.getTransaction().commit();
        session.close();
    }

    // =========================
    // FIND BMI HISTORY BY PERSON
    // =========================
    @Override
    public List<BmiRecord> findByPerson(Integer personId) {
        Session session = openSession();

        List<BmiRecord> list = session.createQuery(
                "FROM BmiRecord b WHERE b.person.id = :pid ORDER BY b.date DESC",
                BmiRecord.class)
                .setParameter("pid", personId)
                .list();

        session.close();
        return list;
    }
}
