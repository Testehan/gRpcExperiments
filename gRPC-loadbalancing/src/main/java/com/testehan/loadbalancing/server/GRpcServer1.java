package com.testehan.loadbalancing.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRpcServer1 {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(6565)
                .addService(new BankService())
                .build();

        server.start();
        System.out.println("gRPC Server1 started");

        server.awaitTermination();
    }
}
