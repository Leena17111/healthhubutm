package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Category;

@Repository
public class CategoryDaoHibernate implements CategoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public List<Category> findAll() {
        Session session = getSession();
        List<Category> list = session
                .createQuery("FROM Category", Category.class)
                .list();
        session.close();
        return list;
    }

    @Override
    public Category findById(Integer id) {
        Session session = getSession();
        Category category = session.get(Category.class, id);
        session.close();
        return category;
    }

    @Override
    public void save(Category category) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(category);   // handles both INSERT + UPDATE
        tx.commit();
        session.close();
    }

    @Override
    public void delete(Integer id) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();

        Category category = session.get(Category.class, id);
        if (category != null) {
            session.delete(category);
        }

        tx.commit();
        session.close();
    }
}
