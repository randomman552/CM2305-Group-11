package com.socialgame.game;

import com.socialgame.game.networking.GameServer;
import com.socialgame.game.networking.Networking;

import java.util.Random;

public class GameCoordinator {
    private static final float SABOTEUR_RATIO = 0.25f;

    private static final int NUM_TASKS_PER_PLAYER = 2;
    private static final int NUM_TASK_TYPES = 2;

    private static final int NUM_ITEMS_PER_PLAYER = 2;
    private static final int NUM_ITEM_TYPES = 5;

    private final Random random = new Random();
    private boolean started = false;
    private int nextID = GameServer.MAX_PLAYERS;


    public int[] pickSaboteurs(int numPlayers) {
        int numSaboteurs = (int) (numPlayers * SABOTEUR_RATIO);
        int[] results = new int[numSaboteurs];
        for(int i = 0; i < numSaboteurs; i++){
            results[i] = random.nextInt(numPlayers);
        }
        return results;
    }

    public Networking.TaskInfo[] generateTasks(int numPlayers) {
        Networking.TaskInfo[] tasks = new Networking.TaskInfo[numPlayers * NUM_TASKS_PER_PLAYER];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new Networking.TaskInfo();
            tasks[i].id = nextID++;
            tasks[i].type = i % NUM_TASK_TYPES;
        }
        return tasks;
    }

    public Networking.ItemInfo[] generateItems(int numPlayers) {
        Networking.ItemInfo[] items = new Networking.ItemInfo[numPlayers * NUM_ITEMS_PER_PLAYER];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Networking.ItemInfo();
            items[i].id = nextID++;
            items[i].type = random.nextInt(NUM_ITEM_TYPES);
        }
        return items;
    }


    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean val) {
        started = val;
    }

    public long getSeed() {
        long seed = random.nextLong();
        random.setSeed(seed);
        return seed;
    }
}
