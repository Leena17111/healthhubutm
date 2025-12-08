package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.dao.PersonDao;
import com.secj3303.model.Person;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonDao personDao;

    public PersonController(PersonDao personDao) {
        this.personDao = personDao;
    }

    // LIST

    @GetMapping("/list")
    public String listPersons(Model model) {
        model.addAttribute("persons", personDao.findAll());
        return "person-list";
    }

    //  ADD FORM 

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("person", new Person());
        return "bmi-form";  
    }

    //  SAVE NEW PERSON 

    @PostMapping("/save")
public String savePerson(@ModelAttribute("person") Person person) {

    // 1. Compute age automatically
    int age = computeAge(person.getYob());
    person.setAge(age);

    // 2. Compute BMI
    calculateBmiAndCategory(person);

    // 3. Save to DB
    personDao.insert(person);

    return "redirect:/person/list";
}


    // EDIT FORM 

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Person p = personDao.findById(id);
        model.addAttribute("person", p);
        return "bmi-form"; 
    }

    //  UPDATE EXISTING 

    @PostMapping("/update")
public String updatePerson(@ModelAttribute("person") Person person) {

    // 1. Compute age again in case YOB was changed
    int age = computeAge(person.getYob());
    person.setAge(age);

    // 2. Recompute BMI again
    calculateBmiAndCategory(person);

    // 3. Update in DB
    personDao.update(person);

    return "redirect:/person/list";
}

    //  DELETE

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        personDao.delete(id);
        return "redirect:/person/list";
    }

    // BMI AND CATEGORY CALCULATION

    private void calculateBmiAndCategory(Person person) {
        if (person.getHeight() > 0) {
            double bmi = person.getWeight() / (person.getHeight() * person.getHeight());
            person.setBmi(bmi);

            String category;
            if (bmi < 18.5) category = "Underweight";
            else if (bmi < 25) category = "Normal";
            else if (bmi < 30) category = "Overweight";
            else category = "Obese";

            person.setCategory(category);
        }
    }

    // Age calculation
    private int computeAge(int yob) {
    int currentYear = java.time.Year.now().getValue();
    return currentYear - yob;
}

}
