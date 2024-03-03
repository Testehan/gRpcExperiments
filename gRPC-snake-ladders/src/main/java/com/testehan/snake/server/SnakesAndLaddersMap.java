package com.testehan.snake.server;

import java.util.HashMap;
import java.util.Map;

public class SnakesAndLaddersMap {

    private static final Map<Integer, Integer> GAME_MAP = new HashMap<>();

    static{
        // ladders  help you win
        GAME_MAP.put(1, 38);
        GAME_MAP.put(4, 14);
        GAME_MAP.put(8, 30);
        GAME_MAP.put(21, 42);
        GAME_MAP.put(28, 76);
        GAME_MAP.put(50, 67);
        GAME_MAP.put(71, 92);
        GAME_MAP.put(80, 99);

        //snakes bring you down
        GAME_MAP.put(32, 10);
        GAME_MAP.put(36, 6);
        GAME_MAP.put(48, 26);
        GAME_MAP.put(62, 18);
        GAME_MAP.put(88, 24);
        GAME_MAP.put(95, 56);
        GAME_MAP.put(97, 78);
    }

    // when this is called, either no snake or ladder is present in position, in which case
    // position is returned, or a snake or ladder is found, in which case the position
    // corresponding to it will be returned
    public static int getPosition(int position){
        var newPosition =  GAME_MAP.getOrDefault(position, position);
        if (newPosition < position) {
            System.out.println("Got bitten by a snake");
        } else if (newPosition > position) {
            System.out.println("Up the ladder we go !");
        }
        return newPosition;
    }

}
