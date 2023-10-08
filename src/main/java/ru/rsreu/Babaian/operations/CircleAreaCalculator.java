package ru.rsreu.Babaian.operations;

public class CircleAreaCalculator {

    public static float calculateCircleArea(float radius){
        if (radius <= 0) {
            return -1;
        }
        return (float)Math.PI * radius * radius;
    }
}
