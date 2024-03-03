package com.testehan.snake.client;

import com.testehan.snake.models.Die;
import com.testehan.snake.models.GameServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class GameClient {

    private GameServiceGrpc.GameServiceStub stub;

    public void  clientGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        GameStateStreamingResponse gameStateStreamingResponse = new GameStateStreamingResponse(latch);

        StreamObserver<Die> dieStreamObserver = this.stub.roll(gameStateStreamingResponse);

        gameStateStreamingResponse.setDieStreamObserver(dieStreamObserver);
        gameStateStreamingResponse.roll();
        latch.await();
    }

    public void setup(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.stub = GameServiceGrpc.newStub(channel);
    }

    public static void main(String[] args) throws InterruptedException {
        GameClient client = new GameClient();
        client.setup();
        client.clientGame();
    }
}
