package by.laskin.reactivecalculator.exception;

import lombok.Getter;

@Getter
public class ScriptExecutionException extends CalculatorException {

    private final int iteration;
    private final int functionId;

    public ScriptExecutionException(int iteration, int functionId, Throwable cause) {
        super(cause.getMessage(), cause);
        this.iteration = iteration;
        this.functionId = functionId;
    }
}
