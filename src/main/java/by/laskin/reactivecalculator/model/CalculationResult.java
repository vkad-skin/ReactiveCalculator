package by.laskin.reactivecalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResult {
    private int iteration;    // Номер итерации
    private int functionId;   // ID функции (1 или 2)
    private String value;     // Результат (число или текст ошибки)
    private long duration;    // Время вычисления в мс
    private int bufferSize;   // Количество ожидающих результатов (для ordered вывода)
}