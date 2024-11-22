package com.spotfinder.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // If there's an error status
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            // Handle specific error codes
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // 404 - Page not found error
                return "error-404"; // Create a specific view for 404
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // 500 - Internal server error
                return "error-500"; // Create a specific view for 500
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // 403 - Forbidden
                return "error-403"; // Create a specific view for 403
            }
        }
        
        // Default error page for any other error
        return "error"; // General error view
    }

    public String getErrorPath() {
        return "/error"; // Required for Spring Boot to know the error endpoint
    }
}
