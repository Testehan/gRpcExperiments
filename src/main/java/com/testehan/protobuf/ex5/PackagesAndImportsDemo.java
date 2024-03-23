package com.testehan.protobuf.ex5;

import com.testehan.models.ex05.common.Address;
import com.testehan.models.ex05.common.Car;
import com.testehan.models.ex05.Person;

public class PackagesAndImportsDemo {

    public static void main(String[] args) {
        var address = Address.newBuilder()
                .setCity("new york")
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
