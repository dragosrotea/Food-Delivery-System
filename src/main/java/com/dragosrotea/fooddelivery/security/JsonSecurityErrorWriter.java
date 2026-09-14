package com.dragosrotea.fooddelivery.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

final class JsonSecurityErrorWriter {

    private JsonSecurityErrorWriter() {
    }

    static void write(
            HttpServletResponse response,
            HttpStatus status,
            String message,
            String path
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
                {
                  "status": %d,
                  "error": "%s",
                  "message": "%s",
                  "path": "%s",
                  "fieldErrors": {}
                }
                """.formatted(
                status.value(),
                escape(status.getReasonPhrase()),
                escape(message),
                escape(path)
        ));
    }

    private static String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace(""", "\\"");
    }
}
