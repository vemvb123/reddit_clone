package io.github.vemvb123.api_gateway.Config;


import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Principal;

@Configuration
public class RateLimitConfig {

    @Bean
    public KeyResolver userKeyResolver() {

        return exchange ->
                exchange.getPrincipal()
                        .map(Principal::getName);
    }
}


