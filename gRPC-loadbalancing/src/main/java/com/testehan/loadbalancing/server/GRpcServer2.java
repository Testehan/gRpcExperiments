package com.testehan.loadbalancing.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRpcServer2 {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(7575)
                .addService(new BankService())
                .build();

        server.start();
        System.out.println("gRPC Server2 started");

        server.awaitTermination();
    }
}
