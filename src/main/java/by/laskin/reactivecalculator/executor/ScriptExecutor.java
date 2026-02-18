package by.laskin.reactivecalculator.executor;

import by.laskin.reactivecalculator.model.CalculationResult;
import org.graalvm.polyglot.PolyglotException;

public interface ScriptExecutor {
    CalculationResult execute(int iteration, int functionId, String script) throws PolyglotException;
}