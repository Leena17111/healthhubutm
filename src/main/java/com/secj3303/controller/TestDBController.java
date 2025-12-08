package com.secj3303.controller;

import com.secj3303.dao.PersonDao;
import com.secj3303.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestDBController {

    private final PersonDao personDao;

    @Autowired
    public TestDBController(PersonDao personDao) {
        this.personDao = personDao;
    }

    //TEST ALL  METHODS FROM PersonDaoJdbc
    // TEST FIND ALL
    @RequestMapping("/test/findall")
    @ResponseBody
    public String testFindAll() {
        System.out.println("=== TEST findAll() ===");

        for (Person p : personDao.findAll()) {
            System.out.println(
                p.getId() + " | " +
                p.getName() + " | " +
                p.getYob() + " | " +
                p.getWeight() + " | " +
                p.getHeight()
            );
        }

        return "Check console for findAll() output!";
    }

    // TEST FIND BY ID
    @RequestMapping("/test/findbyid")
    @ResponseBody
    public String testFindById() {
        System.out.println("=== TEST findById(1) ===");

        Person p = personDao.findById(1);

        if (p != null) {
            System.out.println(
                p.getId() + " | " +
                p.getName() + " | " +
                p.getYob()
            );
        } else {
            System.out.println("Person with ID 1 not found");
        }

        return "Check console for findById() output!";
    }

    // TEST INSERT
    @RequestMapping("/test/insert")
    @ResponseBody
    public String testInsert() {
        System.out.println("=== TEST insert() ===");

        Person p = new Person();
        p.setName("Test User");
        p.setYob(2000);
        p.setAge(25);
        p.setWeight(55);
        p.setHeight(1.65);
        p.setBmi(20.2);
        p.setCategory("Normal");

        personDao.insert(p);

        return "Insert completed. Check DB.";
    }

    // TEST UPDATE
    @RequestMapping("/test/update")
    @ResponseBody
    public String testUpdate() {
        System.out.println("=== TEST update() ===");

        Person p = personDao.findById(1);
        if (p != null) {
            p.setName("Updated Name");
            p.setWeight(70);
            p.setHeight(1.72);
            personDao.update(p);
            return "Update done. Check DB.";
        } else {
            return "Cannot update — Person with ID 1 not found.";
        }
    }

    // TEST DELETE
    @RequestMapping("/test/delete")
    @ResponseBody
    public String testDelete() {
        System.out.println("=== TEST delete() ===");

        personDao.delete(1);

        return "Delete attempted. Check DB.";
    }

}
