// Package declaration for the controller
package com.ashokit.controller;

// Import Spring framework annotations
import org.springframework.stereotype.Controller;  // Marks class as Spring MVC Controller
import org.springframework.web.bind.annotation.GetMapping;  // Annotation for handling HTTP GET requests

// Marks this class as a Spring MVC Controller
// This annotation tells Spring this class handles web requests
@Controller
public class HomeController {
    
    // Handles HTTP GET requests for both "/" and "/home" URLs
    // @GetMapping is a shortcut for @RequestMapping(method = RequestMethod.GET)
    // The array {"/", "/home"} means this method responds to both root URL and /home
    @GetMapping({"/", "/home"})
    public String home() {
        // Returns the view name "index"
        // Spring will look for a template named "index.html" in src/main/resources/templates
        return "index";
    }
}