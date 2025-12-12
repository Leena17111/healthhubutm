package com.secj3303.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.secj3303.dao.BmiDao;
import com.secj3303.model.BmiRecord;
import com.secj3303.model.Person;

@Controller
@RequestMapping("/bmi")
public class BmiController {

    @Autowired
    private BmiDao bmiDao;

    // =========================
    // SHOW BMI FORM
    // =========================
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("person", new Person());
        return "bmi-form";
    }

    // =========================
    // CALCULATE + SAVE BMI
    // =========================
  
@PostMapping("/calc")
public String calcBmi(
        @RequestParam double weight,
        @RequestParam double height,
        Model model) {

    double bmi = weight / (height * height);
    String category;

    if (bmi < 18.5) category = "Underweight";
    else if (bmi <= 24.9) category = "Normal";
    else if (bmi <= 29.9) category = "Overweight";
    else category = "Obese";

    // 🔑 TEMP PERSON (for now)
    Person person = new Person();
    person.setId(1); // MUST EXIST IN hhtum_person table

    // 🔑 CREATE BMI RECORD
    BmiRecord record = new BmiRecord();
    record.setPerson(person);
    record.setHeight(height);
    record.setWeight(weight);
    record.setBmiValue(bmi);
    record.setCategory(category);

    // 🔑 SAVE TO DATABASE
    bmiDao.save(record);

    model.addAttribute("bmi", String.format("%.2f", bmi));
    model.addAttribute("status", category);

    return "bmi-result";
}


    // =========================
    // BMI HISTORY
    // =========================
    @GetMapping("/history")
    public String viewHistory(Model model) {

        Integer personId = 1; // temporary
        model.addAttribute("records", bmiDao.findByPerson(personId));

        return "bmi-history";
    }
}
