package com.winston.webflux.handler;

import java.util.UUID;

import com.winston.webflux.req.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    public Mono<User> saveUser(Mono<User> userReq) {

        return userReq.map(user -> {
            UUID uuid = UUID.randomUUID();
            System.out.println("save success and return the user include id :" + uuid);
            user.setId(uuid.toString());
            return user;
        });
    }

}
