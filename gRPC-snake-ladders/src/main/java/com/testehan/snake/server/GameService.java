package com.testehan.snake.server;

import com.testehan.snake.models.*;
import io.grpc.stub.StreamObserver;

public class GameService extends GameServiceGrpc.GameServiceImplBase{
    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        // initial game setup
        var client = Player.newBuilder().setName("client").setPosition(0).build();
        var server = Player.newBuilder().setName("server").setPosition(0).build();

        return new DieStreamingRequest(client, server, responseObserver);
    }
}
