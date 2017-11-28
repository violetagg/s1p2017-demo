package io.springoneplatform.demo.tradingservicewebflux.websocket;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;

import static java.time.Duration.ofSeconds;

public class EchoWebSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(session.receive()
                                   .delayElements(ofSeconds(1))
                                   .log("io.springoneplatform.demo.tradingservicewebflux.websocket"));
    }
}
