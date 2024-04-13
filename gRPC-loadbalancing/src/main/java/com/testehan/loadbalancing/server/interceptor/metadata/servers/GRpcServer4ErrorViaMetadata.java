package com.testehan.loadbalancing.server.interceptor.metadata.servers;

import com.testehan.loadbalancing.server.interceptor.metadata.BankServiceErrorHandlingWithMetadata;
import com.testehan.loadbalancing.server.interceptor.metadata.BankServiceWithMetadata;
import com.testehan.loadbalancing.server.interceptor.metadata.interceptors.UserTokenWithRoleInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRpcServer4ErrorViaMetadata {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(7272)
                .addService(new BankServiceErrorHandlingWithMetadata())
                .build();

        server.start();
        System.out.println("gRPC Server4 started");

        server.awaitTermination();
    }
}
