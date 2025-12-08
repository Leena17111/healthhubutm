package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.Program;

public interface ProgramDao {
    List<Program> findAll();
    Program findById(Integer id);
    void save(Program program); // insert or update
    void delete(Integer id);
}

