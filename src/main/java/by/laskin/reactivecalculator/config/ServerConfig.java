package by.laskin.reactivecalculator.config;

import by.laskin.reactivecalculator.loader.ConfigLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Bean
    public CalculatorFunctionConfig calculatorConfig(ConfigLoader loader) {
        return loader.load();
    }
}