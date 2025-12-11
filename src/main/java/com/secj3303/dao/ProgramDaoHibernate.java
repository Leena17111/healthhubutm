package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Program;

@Repository
public class ProgramDaoHibernate implements ProgramDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session open() {
        return sessionFactory.openSession();
    }

    // =======================
    // 1. FIND ALL
    // =======================
    @Override
    public List<Program> findAll() {
        Session session = open();
        List<Program> list = session
                .createQuery("FROM Program", Program.class)
                .list();
        session.close();
        return list;
    }

    // =======================
    // 2. FIND BY ID
    // =======================
    @Override
    public Program findById(Integer id) {
        Session session = open();
        Program p = session.get(Program.class, id);
        session.close();
        return p;
    }

    // =======================
    // 3. SAVE (Insert or Update)
    // =======================
    @Override
    public void save(Program program) {
        Session session = open();
        session.beginTransaction();

        if (program.getId() == null) {
            // INSERT
            session.save(program);
        } else {
            // UPDATE
            session.update(program);
        }

        session.getTransaction().commit();
        session.close();
    }

    // =======================
    // 4. DELETE
    // =======================
    @Override
    public void delete(Integer id) {
        Session session = open();
        session.beginTransaction();

        Program p = session.get(Program.class, id);
        if (p != null) {
            session.delete(p);
        }

        session.getTransaction().commit();
        session.close();
    }
}
