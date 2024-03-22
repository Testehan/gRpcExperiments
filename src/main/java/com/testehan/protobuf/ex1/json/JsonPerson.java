package com.testehan.protobuf.ex1.json;

public class JsonPerson {

    private String name;
    private int age;

    public JsonPerson(){}

    public JsonPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
