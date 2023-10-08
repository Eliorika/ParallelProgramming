package ru.rsreu.Babaian;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import ru.rsreu.Babaian.operations.CircleAreaCalculator;


public class CircleAreaTest {

    @RepeatedTest(50)
    public void testCircleAreaRadius1(){
        Assertions.assertEquals(3.1415927410125732, CircleAreaCalculator.calculateCircleArea(1));
    }

    @RepeatedTest(50)
    public void testCircleAreaRadius4(){
        Assertions.assertEquals(50.26548385620117, CircleAreaCalculator.calculateCircleArea(4));
    }

    @RepeatedTest(50)
    public void testCircleAreaRadius10(){
        Assertions.assertEquals(314.1592712402344, CircleAreaCalculator.calculateCircleArea(10));
    }

    @RepeatedTest(50)
    public void testCircleAreaRadiusMin5(){
        Assertions.assertEquals(-1, CircleAreaCalculator.calculateCircleArea(-5));
    }
}
