package io.springoneplatform.demo.loadgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@SpringBootApplication
public class LoadGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadGeneratorApplication.class, args);

        Flux.range(1, 200)
            .delayElements(ofMillis(100))
            .flatMap(i -> {
                System.out.println("Request " + i + " started...");
                return WebClient.create("http://localhost:8080/quotes/feed")
                                .get()
                                .accept(TEXT_EVENT_STREAM)
                                .retrieve()
                                .bodyToFlux(Quote.class);
            })
            .blockLast(ofMinutes(5));
    }
}
