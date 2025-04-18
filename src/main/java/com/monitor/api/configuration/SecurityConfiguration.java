package com.monitor.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Stream;

/**
 * Configuration class for application security.
 * <p>
 * This class sets up the Spring Security configuration for OAuth2 login,
 * JWT-based resource server authentication, and method-level security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    /**
     * Configures the HTTP security for the application.
     * <p>
     * It ensures all requests are authenticated, enables OAuth2 login,
     * and configures the application as a resource server using JWT.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the {@link SecurityFilterChain} bean
     * @throws Exception if any error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Custom JWT authentication converter that maps roles from the "spring_sec_roles" claim.
     * <p>
     * This allows the application to recognize and use roles stored in the JWT claims
     * in addition to the default authorities.
     *
     * @return the customized {@link JwtAuthenticationConverter} bean
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles = jwt.getClaimAsStringList("spring_sec_roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });
        return jwtAuthenticationConverter;
    }

    /**
     * Custom OAuth2 user service that adds roles from the "spring_sec_roles" OIDC claim.
     * <p>
     * Enhances the default {@link OidcUserService} by appending extra authorities
     * based on the roles claim provided in the OIDC token.
     *
     * @return the customized {@link OAuth2UserService} for {@link OidcUserRequest} and {@link OidcUser}
     */
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        var oidcUserService = new OidcUserService();
        return userRequest -> {
            var oidcUser = oidcUserService.loadUser(userRequest);
            var roles = oidcUser.getClaimAsStringList("spring_sec_roles");
            var authorities = Stream.concat(oidcUser.getAuthorities().stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }
}
