package com.winston.webflux


import com.winston.webflux.handler.UserHandler
import com.winston.webflux.req.User
import com.winston.webflux.routers.UserRouter
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import spock.lang.Shared
import spock.lang.Specification

class UserHandlerSpec extends Specification {

    WebTestClient testClient

    @InjectMocks
    UserHandler userHandler

    void setup() {
        MockitoAnnotations.initMocks(this)
        def function = routeFunction()
        def routeFunctionSpec = WebTestClient.bindToRouterFunction(function)
        testClient = routeFunctionSpec
                .configureClient()
                .baseUrl("http://127.0.0.1:8089")
                .build()
    }

    def 'test userSave'() {
        given:
            User user = User.builder().name("lina").age(10).build()

        when:
            EntityExchangeResult result = testClient.post().uri("/user")
                    .body(BodyInserters.fromObject(user))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(User.class)
                    .returnResult()
        then:
            noExceptionThrown();
            println(result.getResponseBody())
    }

    def routeFunction() {
        return new UserRouter().userRouterFunction(userHandler);
    }

}
