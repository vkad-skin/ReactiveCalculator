package by.laskin.reactivecalculator.loader;

import by.laskin.reactivecalculator.config.CalculatorFunctionConfig;
import by.laskin.reactivecalculator.exception.ConfigLoadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@Service
public class JsonConfigLoader implements ConfigLoader {

    private final ObjectMapper objectMapper;

    private final String configPath;

    public JsonConfigLoader(ObjectMapper objectMapper,
                            @Value("${json.config.name:}") String configPath) {
        this.objectMapper = objectMapper;
        this.configPath = configPath;
    }

    @Override
    public CalculatorFunctionConfig load()  {
        ClassPathResource resource = new ClassPathResource(configPath);

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, CalculatorFunctionConfig.class);
        } catch (IOException e) {
            throw new ConfigLoadException(configPath, e);
        }
    }
}