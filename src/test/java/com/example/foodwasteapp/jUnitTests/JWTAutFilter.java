package com.example.foodwasteapp.jUnitTests;

import com.example.foodwasteapp.security.JwtAuthFilter;
import com.example.foodwasteapp.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
        import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
        import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;
        import static org.junit.jupiter.api.Assertions.*;

class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthFilter = new JwtAuthFilter(jwtService, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipAuthEndpoint() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/auth/login");

        assertTrue(jwtAuthFilter.shouldNotFilter(request));
    }

    @Test
    void shouldExtractTokenFromHeader() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.isValid("token123")).thenReturn(true);
        when(jwtService.extractUsername("token123")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldExtractTokenFromCookie() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/test");
        Cookie tokenCookie = new Cookie("accessToken", "cookieToken");
        when(request.getCookies()).thenReturn(new Cookie[]{tokenCookie});
        when(jwtService.isValid("cookieToken")).thenReturn(true);
        when(jwtService.extractUsername("cookieToken")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateIfTokenIsInvalid() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.isValid("invalidToken")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthIfAlreadyAuthenticated() throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList()));

        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.isValid("token123")).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        // Provjeravamo da nije ponovno postavljena autentifikacija
        assertEquals("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void shouldContinueFilterChainIfUserNotFound() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.isValid("token123")).thenReturn(true);
        when(jwtService.extractUsername("token123")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenThrow(new UsernameNotFoundException("Not found"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    // Dodajte ove testove u vašu postojeću JwtAuthFilterTest klasu

    @Test
    void shouldContinueFilterChainWhenNoToken() throws ServletException, IOException {
        // Test kada nema ni header ni cookies
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).isValid(anyString());
    }

    @Test
    void shouldContinueFilterChainWhenEmptyAuthorizationHeader() throws ServletException, IOException {
        // Test kada je Authorization header prazan ili nema "Bearer "
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");
        when(request.getCookies()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).isValid(anyString());
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderWithoutToken() throws ServletException, IOException {
        // Test kada je Authorization header samo "Bearer" bez tokena
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer");
        when(request.getCookies()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).isValid(anyString());
    }

    @Test
    void shouldContinueFilterChainWhenEmptyCookieArray() throws ServletException, IOException {
        // Test kada je cookie array prazan
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(new Cookie[0]);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).isValid(anyString());
    }

    @Test
    void shouldContinueFilterChainWhenNoAccessTokenCookie() throws ServletException, IOException {
        // Test kada postoje cookies ali nema "accessToken" cookie
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn(null);
        Cookie otherCookie1 = new Cookie("sessionId", "value1");
        Cookie otherCookie2 = new Cookie("preference", "value2");
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie1, otherCookie2});

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).isValid(anyString());
    }

    @Test
    void shouldFindAccessTokenCookieAmongMultipleCookies() throws ServletException, IOException {
        // Test kada je accessToken cookie između drugih cookie-jeva
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn(null);
        Cookie otherCookie1 = new Cookie("sessionId", "value1");
        Cookie accessTokenCookie = new Cookie("accessToken", "cookieToken123");
        Cookie otherCookie2 = new Cookie("preference", "value2");
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie1, accessTokenCookie, otherCookie2});
        when(jwtService.isValid("cookieToken123")).thenReturn(true);
        when(jwtService.extractUsername("cookieToken123")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtService).isValid("cookieToken123");
    }

    @Test
    void shouldNotProcessWhenTokenIsNull() throws ServletException, IOException {
        // Test edge case kada resolveToken vraća null
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("");
        when(request.getCookies()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).isValid(anyString());
    }

    /*@Test
    void shouldHandleExceptionFromJwtService() throws ServletException, IOException {
        // Test kada jwtService.isValid baca iznimku
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.isValid("token123")).thenThrow(new RuntimeException("JWT parsing error"));

        // Trebao bi proći bez greške i nastaviti filter chain
        assertDoesNotThrow(() -> {
            jwtAuthFilter.doFilterInternal(request, response, filterChain);
        });

        verify(filterChain).doFilter(request, response);
    }*/

    @Test
    void shouldSetAuthenticationDetailsCorrectly() throws ServletException, IOException {
        // Test da se WebAuthenticationDetails postavlja ispravno
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.isValid("token123")).thenReturn(true);
        when(jwtService.extractUsername("token123")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth);
        assertEquals(userDetails, auth.getPrincipal());
        assertNull(auth.getCredentials());
        assertNotNull(auth.getDetails());
        verify(filterChain).doFilter(request, response);
    }
}
