package com.testehan.snake.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.testehan.snake.models.Die;
import com.testehan.snake.models.GameState;
import com.testehan.snake.models.Player;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameStateStreamingResponse implements StreamObserver<GameState> {

    private StreamObserver<Die> dieStreamObserver;
    private CountDownLatch latch;

    public GameStateStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }
    
    @Override
    public void onNext(GameState gameState) {
        var playerList = gameState.getPlayerList();
        playerList.forEach(p -> System.out.println(p.getName() + ":" + p.getPosition()));

        boolean isGameOver = playerList.stream().anyMatch(p -> p.getPosition() == 100);

        if (isGameOver){
            System.out.println("Game Over!");
            this.dieStreamObserver.onCompleted();
        } else {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            this.roll();
        }
        System.out.println("-------------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Game ended !");
        this.latch.countDown();
    }

    public void setDieStreamObserver(StreamObserver<Die> dieStreamObserver) {
        this.dieStreamObserver = dieStreamObserver;
    }

    public void roll() {
        int dieValue = ThreadLocalRandom.current().nextInt(1, 7);
        Die die = Die.newBuilder().setValue(dieValue).build();
        this.dieStreamObserver.onNext(die);
    }
}
