package com.example.foodwasteapp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.example.foodwasteapp.service.impl.JwtService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserDetailsService uds;

    public JwtAuthFilter(JwtService jwt, UserDetailsService uds) {
        this.jwt = jwt; this.uds = uds;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {
        String token = null;
        if (req.getCookies()!=null) {
            for (Cookie c: req.getCookies()) {
                if ("accessToken".equals(c.getName())) token=c.getValue();
            }
        }
        if (token!=null && jwt.validateToken(token)) {
            String user = jwt.getUsername(token);
            if (SecurityContextHolder.getContext().getAuthentication()==null) {
                UserDetails ud = uds.loadUserByUsername(user);
                var auth = new UsernamePasswordAuthenticationToken(
                        ud,null,ud.getAuthorities()
                );
                auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(req)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(req,res);
    }
}
