package deti.tqs.cinemax.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    
    private CustomUserDetailsService userDetailsService;

    private JwtUtilService jwtUtil;

    @Autowired
    public JwtAuthFilter(CustomUserDetailsService userDetailsService, JwtUtilService jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    /**
     * This method is called for each request that comes to the server
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the token from the header
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the token is not null and starts with Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            
            try {
                // Try to extract username using jwtUtil
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                logger.error("Failed to extract username from jwtUtil");
            }
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get the user details from the token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

}
