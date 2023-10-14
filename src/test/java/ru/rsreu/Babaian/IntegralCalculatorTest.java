package ru.rsreu.Babaian;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.operations.IntegralCalculator;
import ru.rsreu.Babaian.operations.IntegralCalculatorTolerance;

import java.util.concurrent.ExecutionException;

public class IntegralCalculatorTest {

    @Test
    public void testIntegralCalculatorTolerance1() throws InterruptedException, ExecutionException {
        IntegralCalculatorTolerance integralCalculatorTolerance = new IntegralCalculatorTolerance(1);
        System.out.println(integralCalculatorTolerance.calculateIntegralFrom0To1());
        Assertions.assertEquals(0.30116867893974614, integralCalculatorTolerance.calculateIntegralFrom0To1(), 1);
        System.out.println("Время выполнения: " + integralCalculatorTolerance.getElapsedTimeSeconds() + " секунд");
    }

    @Test
    public void testIntegralCalculatorTolerance10() throws InterruptedException, ExecutionException {
        IntegralCalculatorTolerance integralCalculatorTolerance = new IntegralCalculatorTolerance( 0.15e-13);
        Assertions.assertEquals(0.30116867893974614, integralCalculatorTolerance.calculateIntegralFrom0To1(), 0.15e-13);
        System.out.println("Время выполнения: " + integralCalculatorTolerance.getElapsedTimeSeconds() + " секунд");
    }

    @Test
    public void testIntegralCalculatorNoTolerance() throws InterruptedException, ExecutionException {
        IntegralCalculator integralCalculator = new IntegralCalculator();
        Assertions.assertEquals(0.30116867893974614, integralCalculator.calculateIntegralParallel(0, 1, 4), 0.15e-13);
        System.out.println("Время выполнения: " + integralCalculator.getElapsedTimeSeconds() + " секунд");
    }


}
