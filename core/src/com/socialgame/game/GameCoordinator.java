package com.socialgame.game;

import com.socialgame.game.networking.Networking;

public class GameCoordinator {
    protected SocialGame game;

    public GameCoordinator(SocialGame game) {
        this.game = game;
    }

    public int getNumSaboteurs() {
        return 3;
    }

    public void pickSaboteurs(Networking.PlayerInfo[] playerInfos) {
        // TODO: MIA: Method random selects a number (returned by getNumSaboteurs()) of players as saboteurs
    }

    public void checkWinConditions() {
        // TODO: Change screen in here when a win condition is met or failed
    }
}
