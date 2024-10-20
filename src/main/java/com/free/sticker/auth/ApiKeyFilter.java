package com.free.sticker.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyFilter extends OncePerRequestFilter {

    private String apiKey = System.getenv("API_KEY");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKeyHeader = request.getHeader("api-key"); // Change this to match your header name

        // Validate the API key
        if (apiKeyHeader != null && apiKeyHeader.equals(apiKey)) {
            // If valid, create an authentication token
            ApiKeyAuthenticationToken auth = new ApiKeyAuthenticationToken(apiKeyHeader);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // If invalid, you might want to set a 401 Unauthorized response
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid API Key");
            return; // Return early
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
