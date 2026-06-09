package com.security.bank.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A filter that intercepts every HTTP request once and checks for a
 * valid JWT token in the {@code Authorization} header. If a valid token is
 * found, the user is authenticated and added to the Spring Security context.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    private final JwtAuthenticationHelper jwtHelper;

    /**
     * Constructs the filter with required dependencies.
     *
     * @param userDetailService service to load user details
     * @param jwtHelper         helper for JWT parsing and validation
     */
    public JwtAuthenticationFilter(CustomUserDetailService userDetailService, JwtAuthenticationHelper jwtHelper) {
        this.userDetailService = userDetailService;
        this.jwtHelper = jwtHelper;
    }

    /**
     * Extracts the JWT token from the {@code Authorization} header,
     * validates it, and sets the authentication in the security context
     * if the token is valid and the user is not already authenticated.
     *
     * @param request     incoming HTTP request
     * @param response    HTTP response
     * @param filterChain the filter chain to pass the request/response to the next filter
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Retrieve the Authorization header
        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // Check if the header starts with "Bearer "
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            // Remove "Bearer " prefix to get the token
            token = requestHeader.substring(7);

            try {
                // Extract username from token
                username = jwtHelper.getUsernameFromToken(token);
            } catch (Exception e) {
                // Log or ignore invalid token
                System.out.println("Invalid JWT");
            }
        }

        // If we have a username and the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the full user details
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            // Validate the token against the user details
            if (jwtHelper.validateToken(token, userDetails)) {

                // Create an authentication token
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set authentication details (IP, session, etc.)
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}