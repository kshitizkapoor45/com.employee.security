package com.employee.security.config;

import com.employee.security.util.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/public")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            handleAuthenticationFailure(response,"Authorization header is missing or invalid");
            return;
        }
        String jwt = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        log.info("Extracted username: {}", username);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            handleAuthenticationFailure(response, "User not found or token is invalid.");
            return;
        }

        if(path.startsWith("/api/private")){
            processAdminAuthentication(jwt, username, request, response);
        }

        if(path.startsWith("/api/protected")){
            processAuthentication(jwt, username, request, response);
        }
        filterChain.doFilter(request,response);
    }

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
        log.info("Authentication context set for user: {}", userDetails.getUsername());
    }

    private void processAuthentication(String jwt, String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtService.isTokenValid(jwt, userDetails)) {
            setAuthenticationContext(userDetails, request);
        } else {
            handleAuthenticationFailure(response, "Invalid JWT token.");
        }
    }

    private void processAdminAuthentication(String jwt, String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtService.isTokenValid(jwt, userDetails)) {
            // Check if the user has ADMIN role
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                handleAuthenticationFailure(response, "Access denied: Admin privileges required.");
                return;
            }
            setAuthenticationContext(userDetails, request);
        } else {
            handleAuthenticationFailure(response, "Invalid JWT token.");
        }
    }

    private void handleAuthenticationFailure(HttpServletResponse response,String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setUserMessage(message);
        errorResponse.setDeveloperMessage("Unauthorized Access");
        errorResponse.setErrorCode("401");
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
        log.error("Unauthorized access response sent: {}", message);
    }
}
