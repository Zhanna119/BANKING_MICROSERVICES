package com.example.loan_service.config;

import com.example.loan_service.business.repository.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jakarta.ws.rs.Priorities.USER;
import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    //todo можно сделать свой провайдер
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .formLogin()
//                .disable()
//                //formLogin()
//                .cors()
//                .and()
//                //cors?
//                //logout

                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/api/loans/**")
                .permitAll() //todo что это значит?
                /*.requestMatchers("/api/loans/**").hasAnyRole("ADMIN")
                .requestMatchers("/api/loanPayments/**").hasAnyRole("ADMIN")
                .requestMatchers(GET, "/api/loans/all").hasAnyRole("AUDIT")
                //.requestMatchers(GET, "/api/loans/all").hasAnyRole(Role.AUDIT.name())
                .requestMatchers(GET, "/api/loans/getById/**").hasAnyRole("AUDIT")
                .requestMatchers(GET, "/api/loans/id/**").hasAnyRole("AUDIT")
                .requestMatchers(GET, "/api/loanPayments/**").hasAnyRole("AUDIT")*/
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                //todo что делает фильтр?
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
