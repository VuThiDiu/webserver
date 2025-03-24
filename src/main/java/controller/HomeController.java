package controller;

import annotation.GetMapping;
import annotation.PostMapping;

public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/submit")
    public String submitData(String requestBody){
        return "Data submitted: " + requestBody;
    }
}
