package com.testehan.protobuf.ex4;

import com.testehan.models.ex04.BodyStyle;
import com.testehan.models.ex04.Car;
import com.testehan.models.ex04.Dealer;

public class MapDemo {

    public static void main(String[] args) {
        var car1 = Car.newBuilder()
                .setMake("Volvo")
                .setModel("4x4")
                .setYear(2023)
                .setBodyStyle(BodyStyle.COUPE)
                .build();

        var dealer = Dealer.newBuilder().putInventory(123,car1).build();

        System.out.println(dealer);
        System.out.println(dealer.getInventoryMap().get(123).getBodyStyle());

    }
}
