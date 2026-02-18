package by.laskin.reactivecalculator.service;

import by.laskin.reactivecalculator.config.CalculatorFunctionConfig;
import by.laskin.reactivecalculator.exception.ScriptExecutionException;
import by.laskin.reactivecalculator.executor.ScriptExecutor;
import by.laskin.reactivecalculator.formatter.ResultFormatter;
import by.laskin.reactivecalculator.model.CalculationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CalculatorService {

    private final ScriptExecutor executor;
    private final CalculatorFunctionConfig config;
    private final ResultFormatter csvResultFormatter;

    @Autowired
    public CalculatorService(ScriptExecutor executor, CalculatorFunctionConfig config, ResultFormatter csvResultFormatter) {
        this.executor = executor;
        this.config = config;
        this.csvResultFormatter = csvResultFormatter;
    }

    public Flux<String> getCalculationStream(int count, boolean ordered) {

        AtomicInteger bufferedCountFunc1 = new AtomicInteger(0);
        AtomicInteger bufferedCountFunc2 = new AtomicInteger(0);

        int sizeConcurrency = ordered ? 1 : Integer.MAX_VALUE;

        Flux<Integer> ticks = Flux.interval(Duration.ofMillis(config.getInterval()))
                .onBackpressureBuffer(count)
                .take(count)
                .map(Long::intValue);

        Flux<CalculationResult> f1 = ticks.flatMap(i ->
                Mono.fromCallable(() -> executor.execute(i, 1, config.getFunction1()))
                        .subscribeOn(Schedulers.boundedElastic())
                        .onErrorResume(ScriptExecutionException.class, e ->
                                Mono.just(new CalculationResult(
                                        e.getIteration(),
                                        e.getFunctionId(),
                                        "error: " + (e.getCause() != null ?
                                                e.getCause().getMessage() : e.getMessage()),
                                        0, 0
                                ))
                        )
                        .doOnNext(r -> bufferedCountFunc1.incrementAndGet()),
                sizeConcurrency
        );

        Flux<CalculationResult> f2 = ticks.flatMap(i ->
                Mono.fromCallable(() -> executor.execute(i, 2, config.getFunction2()))
                        .subscribeOn(Schedulers.boundedElastic())
                        .onErrorResume(ScriptExecutionException.class, e ->
                                Mono.just(new CalculationResult(
                                        e.getIteration(),
                                        e.getFunctionId(),
                                        "error: " + (e.getCause() != null ?
                                                e.getCause().getMessage() : e.getMessage()),
                                        0, 0
                                ))
                        )
                        .doOnNext(r -> bufferedCountFunc2.incrementAndGet()),
                sizeConcurrency
        );

        if (ordered) {
            return Flux.zip(f1, f2)
                    .map(t -> csvResultFormatter.formatSyncRow(
                            t.getT1(),
                            t.getT2(),
                            bufferedCountFunc1.decrementAndGet(),
                            bufferedCountFunc2.decrementAndGet())
                    );

        } else {
            return Flux.merge(f1, f2)
                    .map(csvResultFormatter::formatAsyncRow);
        }
    }
}
