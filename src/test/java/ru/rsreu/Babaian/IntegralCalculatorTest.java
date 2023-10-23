package ru.rsreu.Babaian;

import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.operations.IntegralCalculator;

import java.util.concurrent.ExecutionException;

public class IntegralCalculatorTest {

    @Test
    public void testIntegralCalculator() throws InterruptedException, ExecutionException {
        int maxTask = 6;
        IntegralCalculator integralCalculator = new IntegralCalculator(maxTask);
        System.out.println(integralCalculator.calculateIntegralParallel(0, 1, 6));
        System.out.println("Время выполнения: " + integralCalculator.getElapsedTimeSeconds() + " секунд");
    }
    @Test
    public void testIntegralCalculatorNoParallel() throws InterruptedException, ExecutionException {
        int maxTask = 6;
        IntegralCalculator integralCalculator = new IntegralCalculator(maxTask);
        System.out.println(integralCalculator.calculateIntegral(0, 1, 6));
        System.out.println("Время выполнения: " + integralCalculator.getElapsedTimeSeconds() + " секунд");
    }

}
