package com.ashokit.controller;

// Import necessary classes for Servlet and Spring MVC
import jakarta.servlet.RequestDispatcher;     // For accessing standard error attributes
import jakarta.servlet.http.HttpServletRequest;  // To handle HTTP requests
import org.springframework.boot.web.servlet.error.ErrorController;  // Spring Boot's error handling interface
import org.springframework.stereotype.Controller;  // Marks this as a Spring MVC controller
import org.springframework.ui.Model;  // For adding attributes to the view
import org.springframework.web.bind.annotation.RequestMapping;  // URL mapping annotation

// Mark this class as a Spring MVC Controller
@Controller
// Implement ErrorController to override Spring Boot's default error handling
public class CustomErrorController implements ErrorController {

    // Map this method to handle all requests to /error
    // This is Spring Boot's default error handling path
    @RequestMapping("/error")
    public String handleError(
            // Inject the HTTP request object to access error details
            HttpServletRequest request,
            // Inject the Model to pass data to the view
            Model model) {
        
        // Get the HTTP status code from the request attributes
        // RequestDispatcher.ERROR_STATUS_CODE is a standard servlet attribute
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        // Get the error message from the request attributes
        // RequestDispatcher.ERROR_MESSAGE contains the error description
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        
        // If status code is available, add it to the model
        if (status != null) {
            // Convert status to string and add to model for view rendering
            model.addAttribute("status", status.toString());
        }
        
        // If error message is available, add it to the model
        if (message != null) {
            // Convert message to string and add to model for view rendering
            model.addAttribute("message", message.toString());
        }
        
        // Return the name of the error view template to render
        // This will look for error.html or error.jsp in the templates directory
        return "error";
    }
}