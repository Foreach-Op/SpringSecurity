package com.foreach.config;

import com.foreach.exceptionhandling.CustomAccessDeniedHandler;
import com.foreach.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.foreach.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer->corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }
        })).csrf(csrfConfig->csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/register", "/apiLogin")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);
        // http.authorizeHttpRequests((requests)->requests.anyRequest().permitAll());
        // http.authorizeHttpRequests((requests)->requests.anyRequest().denyAll());
        // Only HTTPS
        http.sessionManagement(smc->smc.invalidSessionUrl("/invalidSession").maximumSessions(1))
                .requiresChannel(rcc->rcc.anyRequest().requiresSecure()) // Only HTTPS
                .csrf(csrfConfig->csrfConfig.disable())
                .authorizeHttpRequests((requests)->requests
                .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                .requestMatchers("/myBalance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")
                .requestMatchers("/myLoans").authenticated()
                .requestMatchers("/myCards").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/user").authenticated()
                .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession", "/apiLogin").permitAll());
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(hbc->hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc->ehc.accessDeniedHandler(new CustomAccessDeniedHandler())); // It is a global config
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker(){
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        BankUsernamePwdAuthenticationProvider authenticationProvider = new BankUsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}
