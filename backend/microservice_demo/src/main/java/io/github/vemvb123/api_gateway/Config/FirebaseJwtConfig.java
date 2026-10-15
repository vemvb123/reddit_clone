package io.github.vemvb123.api_gateway.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class FirebaseJwtConfig {


    private final String projectId;

    public FirebaseJwtConfig(
            @Value("${firebase.project-id}") String projectId) {
        this.projectId = projectId;
    }


    @Bean
    public ReactiveJwtDecoder firebaseJwtDecoder() {

        String issuer =
                "https://securetoken.google.com/" + projectId;

        NimbusReactiveJwtDecoder decoder =
                NimbusReactiveJwtDecoder
                        .withIssuerLocation(issuer)
                        .build();

       OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = jwt -> {

            if (jwt.getAudience().contains(projectId)) {
                return OAuth2TokenValidatorResult.success();
            }

            OAuth2Error error = new OAuth2Error(
                    "invalid_token",
                    "The token was not issued for this Firebase project",
                    null
            );



                       return OAuth2TokenValidatorResult.failure(error);
        };

        OAuth2TokenValidator<Jwt> validator =
                new DelegatingOAuth2TokenValidator<>(
                        issuerValidator,
                        audienceValidator
                );

        decoder.setJwtValidator(validator);

        return decoder;
    }



}