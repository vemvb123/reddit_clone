package io.github.vemvb123.api_gateway.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        long startTime = System.currentTimeMillis();

        log.info(
                "Incoming Request | ID={} | Method={} | Path={} | IP={} | User-Agent={}",
                request.getId(),
                request.getMethod(),
                request.getPath(),
                request.getRemoteAddress(),
                request.getHeaders().getFirst("User-Agent")
        );

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    long duration = System.currentTimeMillis() - startTime;

                    HttpStatusCode status = exchange.getResponse().getStatusCode();

                    log.info(
                            "Outgoing Response | ID={} | Status={} | Duration={}ms",
                            request.getId(),
                            status,
                            duration
                    );
                }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

