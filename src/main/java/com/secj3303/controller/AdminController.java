package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.secj3303.model.Person;

@Controller
public class AdminController {
   public AdminController() {
   }

   @GetMapping({"/viewprofile"})
   public ModelAndView method1() {
      ModelAndView mv = new ModelAndView("view-profile.th");
      Person person = new Person("ahmad", 2000, 66.0, 1.66, (String[])null);
      mv.addObject("person", person);
      return mv;
   }
}
