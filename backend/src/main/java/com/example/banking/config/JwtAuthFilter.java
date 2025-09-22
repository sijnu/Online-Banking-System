package com.example.banking.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Extracts JWT from Authorization header, validates it and populates SecurityContext.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Jws<Claims> jws = jwtService.parseToken(token);
                Claims body = jws.getBody();
                String subject = body.getSubject();
                Object roleObj = body.get("role");
                String rawRole = roleObj == null ? null : roleObj.toString();

                if (subject != null && rawRole != null) {
                    String normalizedRole = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;
                    var auth = new UsernamePasswordAuthenticationToken(subject, null,
                            List.of(new SimpleGrantedAuthority(normalizedRole)));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    log.warn("JWT missing subject or role claim");
                }
            } catch (Exception ex) {
                log.warn("Failed to parse/validate JWT token: {}", ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
