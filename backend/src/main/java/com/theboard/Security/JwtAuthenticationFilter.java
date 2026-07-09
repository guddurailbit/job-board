package com.theboard.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService service;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            CustomUserDetailsService service) {

        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain chain)

            throws ServletException, IOException {

        String header =
                request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {

            chain.doFilter(request, response);

            return;
        }

        String token = header.substring(7);

        String email = jwtUtil.extractUsername(token);

        if (email != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    service.loadUserByUsername(email);

            if (jwtUtil.validateToken(
                    token,
                    userDetails.getUsername())) {

                UsernamePasswordAuthenticationToken auth =

                        new UsernamePasswordAuthenticationToken(

                                userDetails,

                                null,

                                userDetails.getAuthorities());

                auth.setDetails(

                        new WebAuthenticationDetailsSource()

                                .buildDetails(request));

                SecurityContextHolder

                        .getContext()

                        .setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }

}