package io.github.vemvb123.api_gateway.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    private final String encodedSecret;

    public JwtConfig(@Value("${security.jwt.secret}") String encodedSecret) {
        this.encodedSecret = encodedSecret;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(encodedSecret);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        NimbusReactiveJwtDecoder decoder =
                NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                        .macAlgorithm(MacAlgorithm.HS256)
                        .build();

        decoder.setJwtValidator(JwtValidators.createDefault());
        return decoder;
    }
}