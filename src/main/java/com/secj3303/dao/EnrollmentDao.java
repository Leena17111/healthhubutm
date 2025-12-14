package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.Enrollment;

public interface EnrollmentDao {

    void save(Enrollment enrollment);

    List<Enrollment> findByMember(Integer memberId);

    Enrollment findByMemberAndProgram(Integer memberId, Integer programId);

    List<Enrollment> findAll();
}
