package com.secj3303.dao;

import java.util.List;
import com.secj3303.model.BmiRecord;

public interface BmiDao {

    // Save a BMI record
    void save(BmiRecord record);

    // Get BMI history for a specific person
    List<BmiRecord> findByPerson(Integer personId);
}
