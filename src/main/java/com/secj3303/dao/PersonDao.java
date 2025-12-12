package com.secj3303.dao;

import com.secj3303.model.Person;

public interface PersonDao {
    Person findByEmailAndPassword(String email, String password);
    Person findById(Integer id);
    void save(Person person);
}
