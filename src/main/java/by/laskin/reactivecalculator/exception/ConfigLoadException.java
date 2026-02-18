package by.laskin.reactivecalculator.exception;

public class ConfigLoadException extends CalculatorException {

    public ConfigLoadException(String path, Throwable cause) {
        super("Не удалось загрузить конфиг по пути: " + path, cause);
    }
}
