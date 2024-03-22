package com.testehan.protobuf.ex4;

import com.testehan.models.ex04.BodyStyle;
import com.testehan.models.ex04.Car;

public class DefaultValuesDemo {
    public static void main(String[] args) {
        var car1 = Car.newBuilder().build();

        System.out.println(car1.getMake().toLowerCase());

        System.out.println(car1.getBodyStyle());    // first value of enum is the default value
        System.out.println(car1.getYear());         // 0 is default
    }
}
