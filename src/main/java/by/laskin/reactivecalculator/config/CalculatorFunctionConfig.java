package by.laskin.reactivecalculator.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorFunctionConfig {

    @JsonProperty("function1")
    private String function1;

    @JsonProperty("function2")
    private String function2;

    @JsonProperty("interval")
    private int interval; // в миллисекундах
}