package be.intecbrussel.jpaonetomanydemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    // Handles all errors by mapping to "/error"
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Retrieves the HTTP status code from the request
        Object status = request.getAttribute("javax.servlet.error.status_code");
        // Default error message
        String errorMessage = "Unexpected error";

        if (status != null) {
            // Converts the status object  to int
            int statusCode = (int) status;
            if (statusCode == 500 || statusCode == 404 || statusCode == 400 || statusCode == 403 || statusCode == 401) {
                // Retrieves the exception that caused the error
                Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
                if (throwable != null) {
                    // If an exception is found, use its message as the error message
                    errorMessage = throwable.getMessage();
                }
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

}