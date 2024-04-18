package com.ivanm.flightadvisor.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppSecurityConfiguration {

  private static final String GROUPS = "groups";
  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";

  private final KeycloakLogoutHandler keycloakLogoutHandler;

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(sessionRegistry());
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(
            (authorize) ->
                authorize
                    .requestMatchers("/airports/**", "/v3/api-docs/**", "/swagger-ui/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"))
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {

    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      var authority = authorities.iterator().next();
      boolean isOidc = authority instanceof OidcUserAuthority;

      if (isOidc) {
        var oidcUserAuthority = (OidcUserAuthority) authority;
        var userInfo = oidcUserAuthority.getUserInfo();

        // Tokens can be configured to return roles under
        // Groups or REALM ACCESS hence have to check both
        if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {

          var realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
          var roles = (List<String>) realmAccess.get(ROLES_CLAIM);
          mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
        } else if (userInfo.hasClaim(GROUPS)) {

          List<String> roles = userInfo.getClaim(GROUPS);
          mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
        }
      } else {

        var oauth2UserAuthority = (OAuth2UserAuthority) authority;
        Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

        if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
          Map<String, Object> realmAccess =
              (Map<String, Object>) userAttributes.get(REALM_ACCESS_CLAIM);
          List<String> roles = (List<String>) realmAccess.get(ROLES_CLAIM);
          mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
        }
      }

      return mappedAuthorities;
    };
  }

  @Bean
  List<SimpleGrantedAuthority> generateAuthoritiesFromClaim(List<String> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
  }
}
