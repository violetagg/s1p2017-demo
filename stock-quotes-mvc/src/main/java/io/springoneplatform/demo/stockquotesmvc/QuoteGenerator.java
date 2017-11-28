package io.springoneplatform.demo.stockquotesmvc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import org.springframework.stereotype.Component;

@Component
public class QuoteGenerator {

    private final MathContext mathContext = new MathContext(2);

    private final Random random = new Random();

    private final List<Quote> prices = new ArrayList<>();

    public QuoteGenerator() {
        prices.add(new Quote("CTXS", 82.26));
        prices.add(new Quote("DELL", 63.74));
        prices.add(new Quote("GOOG", 847.24));
        prices.add(new Quote("MSFT", 65.11));
        prices.add(new Quote("ORCL", 45.71));
        prices.add(new Quote("RHT", 84.29));
        prices.add(new Quote("VMW", 92.21));
    }


    public Flux<Quote> fetchQuoteStream(Duration period) {

        return Flux.generate(() -> 0,
                   (BiFunction<Integer, SynchronousSink<Quote>, Integer>) (index, sink) -> {
                       Quote updatedQuote = updateQuote(prices.get(index));
                       sink.next(updatedQuote);
                       return ++index % prices.size();
                   })
                   .zipWith(Flux.interval(period))
                   .map(t -> t.getT1())
                   .map(quote -> {
                       quote.setInstant(Instant.now());
                       return quote;
                   })
                   .log("io.springoneplatform.demo.stockquotesmvc");
    }

    private Quote updateQuote(Quote quote) {
        BigDecimal priceChange =
                quote.getPrice()
                     .multiply(new BigDecimal(0.05 * random.nextDouble()), mathContext);
        return new Quote(quote.getTicker(), quote.getPrice().add(priceChange));
    }
}
