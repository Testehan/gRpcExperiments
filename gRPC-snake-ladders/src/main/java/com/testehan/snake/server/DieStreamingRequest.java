package com.testehan.snake.server;

import com.testehan.snake.models.Die;
import com.testehan.snake.models.GameState;
import com.testehan.snake.models.Player;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

public class DieStreamingRequest implements StreamObserver<Die> {

    private Player client;
    private Player server;
    private StreamObserver<GameState> gameStateStreamObserver;

    public DieStreamingRequest(Player client, Player server, StreamObserver<GameState> gameStateStreamObserver) {
        this.client = client;
        this.server = server;
        this.gameStateStreamObserver = gameStateStreamObserver;
    }

    @Override
    public void onNext(Die die) {
        this.client = this.getNewPlayerPosition(this.client, die.getValue());
        if(this.client.getPosition() != 100){
            this.server = this.getNewPlayerPosition(this.server, getRandomDieValue());
        }
        this.gameStateStreamObserver.onNext(this.getNewGameState());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.gameStateStreamObserver.onCompleted();
    }

    private GameState getNewGameState(){
        return GameState.newBuilder()
                .addPlayer(this.client)
                .addPlayer(this.server)
                .build();
    }

    private static int getRandomDieValue() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }

    private Player getNewPlayerPosition(Player player, int dieValue){
        int position = player.getPosition() + dieValue;
        position = SnakesAndLaddersMap.getPosition(position);
        if (position <= 100){
            player = player.toBuilder()
                    .setPosition(position)
                    .build();
        }
        return player;
    }
}
