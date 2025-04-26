package com.minilinkr.api.controller.v1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@Controller
public class ApiErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object statusAttribute = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // If there's no status code, something unexpected happened, default to 500
        if (statusAttribute == null) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "internal_server_error",
                    "An unknown error occurred.");
        }

        int statusCode = Integer.parseInt(statusAttribute.toString());
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        return switch (httpStatus) {
            case NOT_FOUND -> buildErrorResponse(HttpStatus.NOT_FOUND,
                    "not_found",
                    "The requested resource was not found.");
            case INTERNAL_SERVER_ERROR -> buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "internal_server_error",
                    "An internal server error occurred.");
            case FORBIDDEN -> buildErrorResponse(HttpStatus.FORBIDDEN,
                    "forbidden",
                    "You do not have permission to access this resource.");
            case BAD_REQUEST -> buildErrorResponse(HttpStatus.BAD_REQUEST,
                    "bad_request",
                    "The request could not be understood or was missing required parameters.");
            case CONFLICT -> buildErrorResponse(
                    HttpStatus.CONFLICT,
                    "conflict",
                    "The alias already exists. Please choose a different one.");
            default ->
                // For status codes you haven't explicitly handled, default to 500
                    buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                            "internal_server_error",
                            "An unexpected error occurred.");
        };
    }

    /**
     * Utility method to build the Slack-like JSON error response:
     *
     * {
     *   "ok": false,
     *   "error": "<error_code>",
     *   "response_metadata": {
     *       "messages": [ "[ERROR] <detailed_message>. Learn more: https://docs.minilinkr.com" ]
     *   }
     * }
     */
    public static ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status,
                                                                   String errorCode,
                                                                   String detailedMessage) {
        // Response body
        Map<String, Object> body = new HashMap<>();
        body.put("ok", false);
        body.put("error", errorCode);

        // Additional metadata
        Map<String, Object> meta = new HashMap<>();
        meta.put("messages", Collections.singletonList(
                String.format("[ERROR] %s Learn more: https://docs.minilinkr.com", detailedMessage)
        ));
        body.put("response_metadata", meta);

        // Return a JSON response with the appropriate status code
        return ResponseEntity.status(status).body(body);
    }
}
