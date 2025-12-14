package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.Person;

public interface PersonDao {
    Person findByEmailAndPassword(String email, String password);
    Person findById(Integer id);
    void save(Person person);
    List<Person> findByRole(String role);
    
}
