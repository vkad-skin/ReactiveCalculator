package by.laskin.reactivecalculator.executor;


import by.laskin.reactivecalculator.exception.ScriptExecutionException;
import by.laskin.reactivecalculator.model.CalculationResult;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Component;

import static reactor.netty.http.HttpConnectionLiveness.log;


@Component
public class GraalVMExecutor implements ScriptExecutor {

    private Context polyglotContext;

    private final String lang;

    public GraalVMExecutor(@org.springframework.beans.factory.annotation.Value("${script.executor.lang}") String lang) {
        this.lang = lang;
    }

    @PostConstruct
    public void init() {
        this.polyglotContext = Context.newBuilder(lang)
                .build();
    }

    @Override
    public synchronized CalculationResult execute(int iteration, int functionId, String script) {

        long startTime = System.currentTimeMillis();

        try {
            Value function = polyglotContext.eval(lang, "(" + script + ")");
            Value result = function.execute(iteration);

            long duration = System.currentTimeMillis() - startTime;
            return new CalculationResult(iteration, functionId, result.toString(), duration, 0);

        } catch (RuntimeException e) {

            log.error("Итерация {}, Функция {}: Ошибка в JS-коде -> {}",
                    iteration, functionId, e.getMessage());
            throw new ScriptExecutionException(iteration, functionId, e);
        }
    }

    @PreDestroy
    public void close() {
        if (polyglotContext != null) {
            polyglotContext.close();
        }
    }
}