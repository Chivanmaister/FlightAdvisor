package com.ivanm.flightadvisor.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiSecurityConfig {

  private static final String OAUTH_SCHEME_NAME = "my_oAuth_security_schema";

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .components(new Components().addSecuritySchemes(OAUTH_SCHEME_NAME, createOauthScheme()))
        .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
  }

  private SecurityScheme createOauthScheme() {
    OAuthFlows flows = createOauthFlows();
    return new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows);
  }

  private OAuthFlows createOauthFlows() {
    OAuthFlow flow = createAuthorizationCodeFlow();
    return new OAuthFlows().implicit(flow);
  }

  private OAuthFlow createAuthorizationCodeFlow() {
    return new OAuthFlow()
        .authorizationUrl(
            "http://localhost:9000/realms/flight-advisor-realm/protocol/openid-connect/auth")
        .scopes(
            new Scopes()
                .addString("read_access", "read data")
                .addString("write_access", "modify data"));
  }
}
