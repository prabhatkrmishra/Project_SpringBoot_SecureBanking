package com.security.bank.config;

import com.security.bank.security.CustomUserDetailService;
import com.security.bank.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for the banking application.
 * Defines JWT filter integration, BCrypt password encoding,
 * DAO authentication, and role‑based URL access rules.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, CustomUserDetailService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Exposes the {@link AuthenticationManager} used during login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * BCrypt password encoder bean for secure password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DAO provider that uses the custom {@link CustomUserDetailService}
     * and the BCrypt encoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Configures the security filter chain:
     * <ul>
     *   <li>Disables CSRF (stateless API)</li>
     *   <li>Permits all requests to /user/register and /user/login</li>
     *   <li>Restricts /admin/** to ROLE_ADMIN</li>
     *   <li>All other requests require authentication</li>
     *   <li>Adds the JWT filter before UsernamePasswordAuthenticationFilter</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authenticationProvider(authenticationProvider()).authorizeRequests().antMatchers("/user/register", "/user/login").permitAll().antMatchers("/admin/**").hasAuthority("ROLE_ADMIN").anyRequest().authenticated();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}