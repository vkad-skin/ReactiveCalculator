package by.laskin.reactivecalculator.formatter;

import by.laskin.reactivecalculator.model.CalculationResult;
import org.springframework.stereotype.Component;

@Component
public class CsvResultFormatter implements ResultFormatter {

    @Override
    public String formatHeader(boolean ordered) {
        return ordered
                ? "iteration,res1,time1,buf1,res2,time2,buf2"
                : "iteration,funcId,result,time";
    }

    @Override
    public String formatAsyncRow(CalculationResult result) {
        return String.format("%d,%d,%s,%dms",
                result.getIteration(),
                result.getFunctionId(),
                result.getValue(),
                result.getDuration());
    }

    @Override
    public String formatSyncRow(CalculationResult resultFunc1, CalculationResult resultFunc2,
                                int countFunc1, int countFunc2) {
        return String.format("%d,%s,%dms,%d,%s,%dms,%d",
                resultFunc1.getIteration(),
                resultFunc1.getValue(),
                resultFunc1.getDuration(),
                countFunc1,
                resultFunc2.getValue(),
                resultFunc2.getDuration(),
                countFunc2);
    }
}
