package com.testehan.loadbalancing.server.interceptor.metadata;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRpcServer1 {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(6565)
                .intercept(new AuthInterceptor())
                .addService(new BankServiceWithMetadata())
                .build();

        server.start();
        System.out.println("gRPC Server1 started");

        server.awaitTermination();
    }
}
