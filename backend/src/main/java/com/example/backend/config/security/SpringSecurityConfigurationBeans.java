package com.example.backend.config.security;

import com.example.backend.config.ErrorMessge;
import com.example.backend.config.security.csrf.SpaCsrfToken;
import com.example.backend.config.security.filters.CsrfCustomFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Generated
@RequiredArgsConstructor
public class SpringSecurityConfigurationBeans {
    @Value("${okta.oauth2.issuer}")
    private String issuer;
    @Value("${okta.oauth2.client-id}")
    private String clientId;
    @Value("${frontend.url}")
    private String frontendDomain;
    @Value("${backend.url}")
    private String backendDomain;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/quotes")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/movingexpress/quotes/request")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/movingexpress/quotes/*/events")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/shipments")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/shipments/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/users")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/security/user-info}")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/quotes/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "api/v1/movingexpress/shipments/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "api/v1/movingexpress/shipments/*/inventories")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "api/v1/movingexpress/shipments/*/inventories/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "api/v1/movingexpress/shipments/*/inventories")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "api/v1/movingexpress/shipments/*/inventories/*/items")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/movingexpress/shipments/*/inventories/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "api/v1/movingexpress/shipments/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "api/v1/movingexpress/shipments/*/inventories/*/items/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/movingexpress/shipments/*/observers")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/shipments/*/observers/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/shipments/*/observers")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/movingexpress/shipments/*/observers/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "api/v1/movingexpress/shipments/*/inventories/*/items/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/movingexpress/shipments/*/observers/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET,"api/v1/movingexpress/shipments/observers/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/movingexpress/shipments/*/observers/inventories")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE,"/api/v1/movingexpress/shipments/*/observers/inventories/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT,"/api/v1/movingexpress/shipments/*/observers/inventories/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT,"/api/v1/movingexpress/shipments/*/observers/inventories/*/items/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE,"/api/v1/movingexpress/shipments/*/observers/inventories/*/items/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/movingexpress/shipments/*/observers/inventories/*/items")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/api/v1/movingexpress/shipments/*/observers/inventories/*/items/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "api/v1/movingexpress/shipments/*/inventories/*/items/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/shipments/assigned/driver")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/shipments/unassigned/driver")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/movingexpress/shipments/driver")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/movingexpress/shipments")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/movingexpress/shipments/setShipmentFinalWeight")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/movingexpress/shipments/*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/movingexpress/shipments/*/driver")).permitAll()

                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint()))
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer.loginPage("/login/oauth2/code/okta")
                        .defaultSuccessUrl(backendDomain + "api/v1/movingexpress/security/redirect", true)
                        .permitAll())
                .oauth2ResourceServer(jwt -> jwt.jwt(withDefaults()))
                .logout(logout -> logout
                        .logoutUrl("/api/v1/movingexpress/logout")
                        .addLogoutHandler(logoutHandler())
                        .logoutSuccessHandler((request, response, authentication) -> {
                            Arrays.stream(request.getCookies()).toList().forEach(cookie -> {
                                if (!cookie.getName().equals("JSESSIONID")) {
                                    Cookie newCookie = new Cookie(cookie.getName(), "");
                                    newCookie.setMaxAge(0);
                                    newCookie.setPath("/");
                                    response.addCookie(newCookie);
                                }
                            });
                            response.setStatus(HttpStatus.OK.value());
                        }))
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(csrfTokenRepository())
                        .csrfTokenRequestHandler(new SpaCsrfToken())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/v1/movingexpress/logout", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/security/redirect", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/quotes/*/events", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/logout", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/security/redirect", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/quotes/request", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/setShipmentFinalWeight", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/users", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/quotes", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories/*", HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories/*/items", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories/*", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories/*", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*", HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories/*/items/*", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/observer/*", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/*", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/*", HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories/*/items/*", HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/*", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/inventories", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/inventories/*", HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/inventories/*", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/inventories/*/items/*", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/inventories/*/items", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/inventories/*/items/*", HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/observers/inventories/*/items/*", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/inventories/*/items/*", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/api/v1/movingexpress/shipments/*/driver", HttpMethod.PUT.toString())

                        )
                )
                .cors(httpSecurityCorsConfigurer -> {
                    final var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of(frontendDomain));
                    cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                    cors.setAllowedHeaders(Arrays.asList("authorization", "content-type", "xsrf-token"));
                    cors.setExposedHeaders(List.of("xsrf-token"));
                    cors.setAllowCredentials(true);
                    cors.setMaxAge(3600L);
                })
                .addFilterAfter(new CsrfCustomFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            final ErrorMessge errorMessage = ErrorMessge.from("Requires authentication");
            final String json = objectMapper.writeValueAsString(errorMessage);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(json);
            response.flushBuffer();
        };
    }

    private LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {

            List<Cookie> cookies = List.of(request.getCookies());

            cookies.forEach(cookie -> {
                Cookie newCookie = new Cookie(cookie.getName(), "");
                newCookie.setMaxAge(0);
                newCookie.setPath("/");
                String formattedDomain = frontendDomain.replace("https://", "").replace("http://", "")
                        .split(":")[0].replace("/", "");
                newCookie.setDomain(formattedDomain);
                response.addCookie(newCookie);
            });

            boolean isSignup = Boolean.parseBoolean(request.getParameter("isLogoutSignUp"));
            boolean isExternal = Boolean.parseBoolean(request.getParameter("isLogoutExternal"));
            try {
                if (isSignup)
                    response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" +backendDomain + "oauth2/authorization/okta");
                else if (isExternal)
                    response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + frontendDomain + "/external");
                else
                    response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + frontendDomain);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
    public CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
        cookieCsrfTokenRepository.setCookieCustomizer(cookie -> {
            cookie.domain(frontendDomain.replace("https://", "").replace("http://", "")
                    .split(":")[0].replace("/", ""));
            cookie.httpOnly(false);
            cookie.path("/");
            cookie.secure(false);

        });
        return cookieCsrfTokenRepository;
    }
}
