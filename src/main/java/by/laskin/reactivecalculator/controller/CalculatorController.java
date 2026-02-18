package by.laskin.reactivecalculator.controller;

import by.laskin.reactivecalculator.formatter.ResultFormatter;
import by.laskin.reactivecalculator.service.CalculatorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final CalculatorService calcService;
    private final ResultFormatter csvResultFormatter;

    public CalculatorController(CalculatorService calcService, ResultFormatter csvResultFormatter) {
        this.calcService = calcService;
        this.csvResultFormatter = csvResultFormatter;
    }

    @CrossOrigin
    @GetMapping(value = "/calculate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> calculate(
            @RequestParam(name = "count") int count,
            @RequestParam(name = "ordered", defaultValue = "true") boolean ordered) {

        return calcService.getCalculationStream(count, ordered)
                .startWith(csvResultFormatter.formatHeader(ordered));
    }

}
