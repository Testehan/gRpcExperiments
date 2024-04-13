package com.testehan.loadbalancing.server.interceptor.metadata.servers;

import com.testehan.loadbalancing.server.interceptor.metadata.BankServiceWithMetadata;
import com.testehan.loadbalancing.server.interceptor.metadata.interceptors.UserTokenInterceptor;
import com.testehan.loadbalancing.server.interceptor.metadata.interceptors.UserTokenWithRoleInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRpcServer2UserTokenIntercept {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(7575)
                .intercept(new UserTokenInterceptor())
                .addService(new BankServiceWithMetadata())
                .build();

        server.start();
        System.out.println("gRPC Server2 started");

        server.awaitTermination();
    }
}
