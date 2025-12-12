package com.secj3303.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Person;

@Repository
public class PersonDaoHibernate implements PersonDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session open() {
        return sessionFactory.openSession();
    }

    // =======================
    // LOGIN QUERY
    // =======================
    @Override
    public Person findByEmailAndPassword(String email, String password) {
        Session session = open();

        Person person = session.createQuery(
                "FROM Person WHERE email = :email AND password = :password",
                Person.class
        )
        .setParameter("email", email)
        .setParameter("password", password)
        .uniqueResult();

        session.close();
        return person;
    }

    // =======================
    // FIND BY ID (BMI FK)
    // =======================
    @Override
    public Person findById(Integer id) {
        Session session = open();
        Person person = session.get(Person.class, id);
        session.close();
        return person;
    }

    @Override
public void save(Person person) {
    Session session = open();
    session.beginTransaction();
    session.save(person);
    session.getTransaction().commit();
    session.close();
}

}
