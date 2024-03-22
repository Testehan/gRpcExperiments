package com.testehan.protobuf.ex1;

// comparing JSON vs protobuf serialization and deserialization

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.testehan.models.ex01.Person;
import com.testehan.protobuf.ex1.json.JsonPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PerformanceTestDemo {

    private static final Logger log = LoggerFactory.getLogger(PerformanceTestDemo.class);

    public static void main(String[] args) {

        // json
        var jsonPerson = new JsonPerson("sam", 12);
        Runnable jsonRunnable = getJsonRunnable(jsonPerson);


        // protobuf
        var person = Person.newBuilder().setName("sam").setAge(12).build();

        Runnable protoRunnable = getProtoRunnable(person);

        // start testing
        for (int i = 0; i < 5; i++) {   // 5 iterations of the test
            System.out.println("Test number " + i);
            runTest("json", jsonRunnable);
            runTest("proto", protoRunnable);
        }
    }

    private static Runnable getProtoRunnable(Person person) {
        Runnable protoRunnable = () -> {
            byte[] bytesRepresentationOfPerson = person.toByteArray();
            try {
                Person.parseFrom(bytesRepresentationOfPerson);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        };
        return protoRunnable;
    }

    private static Runnable getJsonRunnable(JsonPerson jsonPerson) {
        ObjectMapper mapper = new ObjectMapper();

        Runnable jsonRunnable = () -> {
            try {
                byte[] bytes = mapper.writeValueAsBytes(jsonPerson);
                var jsonPersonRead = mapper.readValue(bytes, JsonPerson.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return jsonRunnable;
    }

    private static void runTest(String testName, Runnable runnable){
        var start = System.currentTimeMillis();
        for (int i = 0; i < 5_000_000; i++) {
            runnable.run();
        }
        var end = System.currentTimeMillis();
        log.info("time taken for {} - {} ms", testName, (end - start));
    }
}
