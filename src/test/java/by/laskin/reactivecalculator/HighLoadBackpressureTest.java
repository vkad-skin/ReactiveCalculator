package by.laskin.reactivecalculator;

import by.laskin.reactivecalculator.service.CalculatorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
class HighLoadBackpressureTest {

    @Autowired
    private CalculatorService calculatorService;

    @Test
    @DisplayName("Тест на выносливость (sync): 10 000 итераций без переполнения")
    void shouldHandleHighLoadWithoutOverflowInSync() {
        int count = 10000;
        boolean ordered = true;

        var resultStream = calculatorService.getCalculationStream(count, ordered);

        StepVerifier.create(resultStream)
                .expectSubscription()
                .expectNextCount(count)
                .expectComplete()
                .verify(Duration.ofMinutes(2));
    }

    @Test
    @DisplayName("Тест на выносливость (async): 10 000 итераций без переполнения")
    void shouldHandleHighLoadWithoutOverflowInAsync() {
        int count = 10000;
        boolean ordered = false;

        var resultStream = calculatorService.getCalculationStream(count, ordered);

        int expectedLines = count * 2;

        StepVerifier.create(resultStream)
                .expectSubscription()
                .expectNextCount(expectedLines)
                .expectComplete()
                .verify(Duration.ofMinutes(2));
    }
}