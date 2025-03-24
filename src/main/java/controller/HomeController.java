package controller;

import annotation.Controller;
import annotation.http.GetMapping;
import annotation.http.PostMapping;


@Controller
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
    public String submitData(String requestBody) {
        return requestBody;
    }
}
