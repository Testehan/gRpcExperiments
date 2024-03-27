package com.testehan.protobuf.ex7;

// purpose of this is to experiment what happens when a proto file that has been
// distributed to various clients changes fields / types. How do the clients react ?
// Do they still work ?
// IN ORDER To run various methods from main method, you must change the import
// "import com.testehan.models.ex06.v1.Television;"  to v1 v2 v3 v4..

import com.testehan.models.ex07.v3.Television;
import com.testehan.models.ex07.v2.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VersioningDemo {

    public static void main(String[] args) throws IOException {
//        v1_createTelevisionAndWriteToFile();
//        v2_readTelevisionV1FileUsingV2();
//        v2_createTelevisionV2AndWriteToFile();
//        v1_readTelevisionV2FileUsingV1();
        v3_readTelevisionV1FileUsingV3();
    }

//    private static void v1_createTelevisionAndWriteToFile() throws IOException {
//        // this should use com.testehan.models.ex07.v1.Television
//
//        var television = Television.newBuilder().setBrand("Samsung").setYear(2023).build();
//
//        // serialization        // the extension of the file can be other as well...it is just random here
//        var path = Paths.get("/Users/danteshte/JavaProjects/gRpcExperiments/gRPC-start/target/television.serialized");
//        Files.write(path, television.toByteArray());
//
//        // deserialization
//        byte[] bytesRepresentationOfTelevision = Files.readAllBytes(path);
//        var televisionRead = Television.parseFrom(bytesRepresentationOfTelevision);
//        System.out.println(televisionRead);
//    }

//    private static void v2_readTelevisionV1FileUsingV2() throws IOException {
//        // this should use com.testehan.models.ex07.v2.Television
//
//        // serialization        // the extension of the file can be other as well...it is just random here
//        var path = Paths.get("/Users/danteshte/JavaProjects/gRpcExperiments/gRPC-start/target/television.serialized");
//
//        // deserialization
//        byte[] bytesRepresentationOfTelevision = Files.readAllBytes(path);
//        var televisionRead = Television.parseFrom(bytesRepresentationOfTelevision);
//        System.out.println(televisionRead);     // will print instead of "year", "model" as we changed the name of the field
//        System.out.println(televisionRead.getType());   // HD is default
//    }

//    private static void v2_createTelevisionV2AndWriteToFile() throws IOException {
//        // this should use com.testehan.models.ex07.v2.Television
//
//        var television = Television.newBuilder()
//                .setBrand("Samsung").setModel(2023).setType(Type.OLED).build();
//
//        // serialization        // the extension of the file can be other as well...it is just random here
//        var path = Paths.get("/Users/danteshte/JavaProjects/gRpcExperiments/gRPC-start/target/televisionV2.serialized");
//        Files.write(path, television.toByteArray());
//
//        // deserialization
//        byte[] bytesRepresentationOfTelevision = Files.readAllBytes(path);
//        var televisionRead = Television.parseFrom(bytesRepresentationOfTelevision);
//        System.out.println(televisionRead);
//    }

//    private static void v1_readTelevisionV2FileUsingV1() throws IOException {
//        // this should use com.testehan.models.ex07.v1.Television
//
//        // serialization        // the extension of the file can be other as well...it is just random here
//        var path = Paths.get("/Users/danteshte/JavaProjects/gRpcExperiments/gRPC-start/target/televisionV2.serialized");
//
//        // deserialization
//        byte[] bytesRepresentationOfTelevision = Files.readAllBytes(path);
//        var televisionRead = Television.parseFrom(bytesRepresentationOfTelevision);
//        System.out.println(televisionRead);     // will print 3 instead of "type" as v1 does not know about "type" field, because it was introduced in v2
//
//    }

    private static void v3_readTelevisionV1FileUsingV3() throws IOException {
        // this should use com.testehan.models.ex07.v3.Television

        // serialization        // the extension of the file can be other as well...it is just random here
        var path = Paths.get("/Users/danteshte/JavaProjects/gRpcExperiments/gRPC-start/target/television.serialized");

        // deserialization
        byte[] bytesRepresentationOfTelevision = Files.readAllBytes(path);
        var televisionRead = Television.parseFrom(bytesRepresentationOfTelevision);
        System.out.println(televisionRead);     // will print "2" instead of "year" the field was deleted in v3

    }
}
