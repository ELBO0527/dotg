package elbo.dotg.api17.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final String[] accessUrls = {"/error", "/api/v1/signin", "/api/v1/signup"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests(authorizes ->
                        authorizes
                                .requestMatchers("/api/v1/categories/**", "/api/v1/users/**").hasRole("USER")
                                .requestMatchers(accessUrls).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                                .accessDeniedHandler(new AccessDeniedHandler() {
                                    @Override
                                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                        response.sendError(HttpServletResponse.SC_FORBIDDEN);

                                        response.getWriter();
                                    }
                                })
                                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                                    @Override
                                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                                        response.getWriter();
                                    }
                                })
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);;

        return http.build();
    }
}
