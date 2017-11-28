package io.springoneplatform.demo.stockquotesmvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static java.time.Duration.ofMillis;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

@RestController
public class QuoteController {

    private final Flux<Quote> quoteStream;

    public QuoteController(QuoteGenerator quoteGenerator) {
        this.quoteStream = quoteGenerator.fetchQuoteStream(ofMillis(200))
                                         .share();
    }

    @GetMapping(path = "/quotes", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<Quote> streamQuotes() {
        return quoteStream;
    }
}
