package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom PRE Filter
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Logging PRE baseMessage: {}", config.getBaseMessage());
//            if (config.isPreLogger()) {
//                log.info("Logging Filter Start: request id -> {}",request.getId());
//            }
//            //Global PRE -> Custom PRE,POST -> Global POST
//            //Custom Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                if(config.isPostLogger()){
//                    log.info("Logging Filter End: response code -> {}", response.getStatusCode());
//                }
//            }));
//        };
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging PRE baseMessage: {}", config.getBaseMessage());
            if (config.isPreLogger()) {
                log.info("Logging Filter Start: request id -> {}", request.getId());
            }
            //Global PRE -> Custom PRE,POST -> Global POST
            //Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Logging Filter End: response code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.LOWEST_PRECEDENCE); // 우선순위 조절하여 실행순서 조절 가능
        //,Ordered.HIGHEST_PRECEDENCE); // 우선순위 조절하여 실행순서 조절 가능
        // HIGHEST_PRECEDENCE: Logging->Global->Custom
        // LOWEST_PRECEDENCE: Global->Custom->Logging
        return filter;
    }

    @Data
    public static class Config {
        // Put the configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
