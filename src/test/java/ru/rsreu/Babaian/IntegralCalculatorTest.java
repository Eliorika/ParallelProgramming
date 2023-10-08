package ru.rsreu.Babaian;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.operations.IntegralCalculator;

import java.util.concurrent.ExecutionException;

public class IntegralCalculatorTest {

    @Test
    public void testIntegralCalculatorTolerance1() throws InterruptedException, ExecutionException {
        IntegralCalculator integralCalculator = new IntegralCalculator(1);
        System.out.println(integralCalculator.calculateIntegralFrom0To1());
        Assertions.assertEquals(0.30116867893974614, integralCalculator.calculateIntegralFrom0To1(), 1);
        System.out.println("Время выполнения: " + integralCalculator.getElapsedTimeSeconds() + " секунд");
    }

    @Test
    public void testIntegralCalculatorTolerance10() throws InterruptedException, ExecutionException {
        IntegralCalculator integralCalculator = new IntegralCalculator( 0.15e-13);
        Assertions.assertEquals(0.30116867893974614, integralCalculator.calculateIntegralFrom0To1(), 0.15e-13);
        System.out.println("Время выполнения: " + integralCalculator.getElapsedTimeSeconds() + " секунд");
    }
}
