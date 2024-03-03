package com.testehan.snake.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.Executors;

public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(6565)
                    // i think the executor part is needed so that at most 20 players can play vs the server in parallel ?
                .executor(Executors.newFixedThreadPool(20))
                .addService(new GameService())
                .build();

        server.start();

        System.out.println("Server started!");

        server.awaitTermination();

    }
}
