package com.secj3303.dao;

import com.secj3303.model.Person;
import java.util.List;

public interface PersonDao {
    List<Person> findAll();
    Person findById(int id);
    void insert(Person p);
    void update(Person p);
    void delete(int id);
}
