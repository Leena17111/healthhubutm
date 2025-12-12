package com.secj3303.controller;

import javax.servlet.http.HttpSession;

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
        HttpSession session,
        Model model) {

    // 🔑 GET LOGGED-IN PERSON
    Person person = (Person) session.getAttribute("loggedUser");

    if (person == null) {
        return "redirect:/login";
    }

    double bmi = weight / (height * height);
    String category;

    if (bmi < 18.5) category = "Underweight";
    else if (bmi <= 24.9) category = "Normal";
    else if (bmi <= 29.9) category = "Overweight";
    else category = "Obese";

    BmiRecord record = new BmiRecord();
    record.setPerson(person);   // ✅ REAL person
    record.setHeight(height);
    record.setWeight(weight);
    record.setBmiValue(bmi);
    record.setCategory(category);

    bmiDao.save(record);

    model.addAttribute("bmi", String.format("%.2f", bmi));
    model.addAttribute("status", category);

    return "bmi-result";
}



    // =========================
    // BMI HISTORY
    // =========================
    @GetMapping("/history")
public String viewHistory(HttpSession session, Model model) {

    Person person = (Person) session.getAttribute("loggedUser");

    if (person == null) {
        return "redirect:/login";
    }

    model.addAttribute("records",
            bmiDao.findByPerson(person.getId()));

    return "bmi-history";
}

}
