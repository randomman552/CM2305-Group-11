package com.socialgame.game;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerInfluencer;
import com.socialgame.game.networking.GameServer;
import com.socialgame.game.networking.Networking;
import com.socialgame.game.screens.LoseScreen;
import com.socialgame.game.screens.WinScreen;
import com.socialgame.game.player.Player;

import java.util.Random;

public class GameCoordinator {
    Random random = new Random();

    private static final float SABOTEUR_RATIO = 0.25f;

    public int getNumSaboteurs() {
        return GameServer.MAX_PLAYERS;
    }

    public int[] pickSaboteurs(int numPlayers) {
        int numSaboteurs = (int) (numPlayers * SABOTEUR_RATIO);
        int[] results = new int[numSaboteurs];
        for(int i = 0; i < numSaboteurs; i++){
            results[i] = random.nextInt(numPlayers - 1) + 1;
        }
        return results;
    }

    public void checkWinConditions() {
        /*// TODO: Change screen in here when a win condition is met or failed
        if (game.mainPlayer.getIsSaboteur() && numSum - numSaboteurs == 0 || !game.mainPlayer.getIsSaboteur() && numSaboteurs == 0){
            game.setScreen(new WinScreen(game));
        }
        if(!game.mainPlayer.getIsSaboteur() && numSum - numSaboteurs == 0 || game.mainPlayer.getIsSaboteur() && numSaboteurs == 0){
            game.setScreen(new LoseScreen(game));
        }*/
    }
}
