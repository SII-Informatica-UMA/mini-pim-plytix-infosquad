package minipimplytixinfosquad.entidades.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfguration {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(cs -> cs.disable())
            .formLogin(formLogin ->formLogin.disable())
            .httpBasic(httpBasic ->httpBasic.disable())
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                        .requestMatchers("/sin-auth").permitAll()
                        .anyRequest().authenticated()
            )

            .sessionManagement(sessionManagement ->
                    sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        http.addFilterAfter(jwtRequestFilter, LogoutFilter.class);
        return http.build();
    }

    public static Optional<UserDetails> getAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> (UserDetails) authentication.getPrincipal());
    }
}