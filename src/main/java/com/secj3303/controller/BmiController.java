package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.secj3303.model.Person;

@Controller
@RequestMapping("/bmi")
public class BmiController {

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("person", new Person());
        return "bmi-form";
    }


	@PostMapping("/bmi/calc")
	public String calcBmi(double weight, double height, Model model) {

	    double bmi = weight / (height * height);
	    String status;

	    if (bmi < 18.5) status = "Underweight";
	    else if (bmi <= 24.9) status = "Normal";
	    else if (bmi <= 29.9) status = "Overweight";
	    else status = "Obese";

	    model.addAttribute("bmi", String.format("%.2f", bmi));
	    model.addAttribute("status", status);

	    return "bmi-result";  // bmi-result.html
	}

	
	

}
