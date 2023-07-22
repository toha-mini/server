package com.example.todayshouse.security.Jwt;

import com.example.todayshouse.security.userdetails.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getToken(request);

        if (hasNotToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String extractedToken = jwtUtil.extractToken(token);

        if (isInvalidToken(extractedToken)) {
            log.info("token error");
            response.setStatus(403);
            response.getWriter().write("wrong token");
            return;
        }

        Claims claims = jwtUtil.getClaims(extractedToken);

        try {
            setAuthentication(claims.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private boolean isInvalidToken(String extractedToken) {
        return !jwtUtil.validateToken(extractedToken);
    }

    private boolean hasNotToken(String token) {
        return token == null;
    }
}
