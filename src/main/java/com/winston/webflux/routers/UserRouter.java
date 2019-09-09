package com.winston.webflux.routers;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.winston.webflux.req.User;
import com.winston.webflux.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler) {

        return route().path("/user",
          builder -> builder.nest(accept(MediaType.ALL),
            route -> route.POST("/", req -> {
                Mono<User> userReq = req.bodyToMono(User.class);
                Mono<User> userMono = handler.saveUser(userReq);
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userMono, User.class);
            })
          )).build();
    }
}
