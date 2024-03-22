package com.testehan.protobuf.ex1;

import com.testehan.models.ex01.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PersonDemo {

    public static void main(String[] args) throws IOException {
        var person = Person.newBuilder().setName("Dan").setAge(34).build();
        System.out.println(person);

        var person2 = Person.newBuilder().setName("Dan").setAge(34).build();
        System.out.println(person.equals(person2));     // true

        // serialization        // the extension of the file can be other as well...it is just random here
        var path = Paths.get("/Users/danteshte/JavaProjects/gRpcExperiments/target/person.serialized");
        Files.write(path, person.toByteArray());

        // deserialization
        byte[] bytesRepresentationOfPerson = Files.readAllBytes(path);
        var personRead = Person.parseFrom(bytesRepresentationOfPerson);
        System.out.println(personRead);
    }
}
