package by.laskin.reactivecalculator.formatter;

import by.laskin.reactivecalculator.model.CalculationResult;

public interface ResultFormatter {
    String formatHeader(boolean ordered);

    String formatAsyncRow(CalculationResult result);

    String formatSyncRow(CalculationResult resultFunc1, CalculationResult resultFunc2,
                         int countFunc1, int countFunc2);
}
