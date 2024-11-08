package com.guccikray.trackYourHabits.jwt;

import com.guccikray.trackYourHabits.exceptions.UserNotFoundException;
import com.guccikray.trackYourHabits.services.CustomUserDetailsService;
import com.guccikray.trackYourHabits.userDetails.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;


    @Autowired
    public JwtRequestFilter(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        UUID userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // index = 7 cause we need take token without "Bearer "
            String userIdString = jwtUtil.extractUserId(jwt);
            userId = UUID.fromString(userIdString);
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) { // checks if user not authenticated
            try {
                CustomUserDetails customUserDetails = this.customUserDetailsService.loadUserById(userId);

                if (jwtUtil.validateToken(jwt, userId)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            customUserDetails,
                            null,
                            customUserDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);  // updating SecurityContextHolder
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                }
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
