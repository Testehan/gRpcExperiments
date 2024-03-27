package com.testehan.protobuf.ex2;

import com.testehan.models.ex02.Address;
import com.testehan.models.ex02.Car;
import com.testehan.models.ex02.Person;

public class CompositionDemo {

    public static void main(String[] args) {
        var address = Address.newBuilder()
                .setCity("new york")
                .setPostbox(2)
                .setStreet("passion").build();

        var car = Car.newBuilder()
                .setMake("Toyote")
                .setModel("4x4")
                .setYear(2004).build();

        var person = Person.newBuilder()
                .setAddress(address)
                .setCar(car)
                .setName("voldemort")
                .setAge(100).build();

        System.out.println(person);
    }
}
