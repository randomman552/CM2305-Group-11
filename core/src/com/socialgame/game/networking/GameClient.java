package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Item;
import com.socialgame.game.player.Player;
import com.socialgame.game.screens.GameScreen;
import com.socialgame.game.tasks.Task;

import java.io.IOException;

public class GameClient extends Client {
    public static class GameClientListener extends Listener {
        private final SocialGame game;
        private final String password;

        public GameClientListener(SocialGame game, String password) {
            this.game = game;
            this.password = password;
        }

        @Override
        public void received(Connection connection, Object object) {
            System.out.println("Client Receives: " + object);
            if (object instanceof Networking.VelocityUpdate) {
                Networking.VelocityUpdate update = ((Networking.VelocityUpdate) object);
                try {
                    GameObject obj = GameObject.objects.get(update.id);
                    synchronized (game.getPhysWorld()) {
                        obj.body.setLinearVelocity(update.xVel, update.yVel);
                    }
                } catch (NullPointerException ignored) {}
            }
            else if (object instanceof Networking.PositionUpdate) {
                Networking.PositionUpdate update = ((Networking.PositionUpdate) object);
                try {
                    GameObject obj = GameObject.objects.get(update.id);
                    synchronized (game.getPhysWorld()) {
                        obj.setPositionAboutOrigin(update.x, update.y);
                    }
                } catch (NullPointerException ignored) {}
            }
            else if (object instanceof Networking.PickupItemUpdate) {
                Networking.PickupItemUpdate update = ((Networking.PickupItemUpdate) object);
                GameObject player = GameObject.objects.get(update.playerID);
                GameObject item = GameObject.objects.get(update.itemID);
                if (item instanceof Item)
                    ((Item) item).interact(player);
            }
            else if (object instanceof Networking.DropItemUpdate) {
                Networking.DropItemUpdate update = (Networking.DropItemUpdate) object;
                Player player = (Player) GameObject.objects.get(update.playerID);
                player.dropItem();
            }
            else if (object instanceof Networking.SwitchItemUpdate) {
                Networking.SwitchItemUpdate update = (Networking.SwitchItemUpdate) object;
                Player player = (Player) GameObject.objects.get(update.playerID);
                player.setInvSlot(update.newSlot);
            }
            else if (object instanceof Networking.JoinRefused) {
                game.showErrorMessage(((Networking.JoinRefused) object).reason);
            }
            else if (object instanceof Networking.JoinAccepted) {
                Networking.JoinAccepted update = ((Networking.JoinAccepted) object);

                // If our mainPlayer has not been set, initial sync is required
                if (game.mainPlayer == null) {
                    game.setMainPlayer(new Player(game, update.playerID));

                    for (int i = 0; i < GameServer.MAX_PLAYERS; i++) {
                        if (i != update.playerID && update.playerInfos[i] != null) {
                            Networking.PlayerInfo info = update.playerInfos[i];
                            Player newPlayer = new Player(game, i, info.getCustomisation());
                            newPlayer.setPositionAboutOrigin(info.x, info.y);

                            // Inventory sync
                            for (Networking.ItemInfo itemInfo: info.inventory) {
                                if (itemInfo == null) continue;

                                GameObject item = GameObject.objects.get(itemInfo.id);
                                if (item instanceof Item)
                                    ((Item) item).interact(newPlayer);
                            }

                            // Add to stage
                            game.getMainStage().addActor(newPlayer);
                        }
                    }
                } else {
                    Networking.PlayerInfo info = update.playerInfos[update.playerID];
                    Player newPlayer = new Player(game, update.playerID, update.playerInfos[update.playerID].getCustomisation());
                    newPlayer.setPositionAboutOrigin(info.x, info.y);
                    game.getHud().getChat().receiveMessage("Game", newPlayer.getName() + " joined.");
                    game.getMainStage().addActor(newPlayer);
                }
            }
            else if (object instanceof Networking.PlayerTakeDamageUpdate) {
                Networking.PlayerTakeDamageUpdate update = (Networking.PlayerTakeDamageUpdate) object;
                Player player = (Player) GameObject.objects.get(update.playerID);
                player.takeDamage(update.damage);
            }
            else if (object instanceof Networking.LeaveNotification) {
                Networking.LeaveNotification update = ((Networking.LeaveNotification) object);
                GameObject player = GameObject.objects.get(update.playerID);
                game.getHud().getChat().receiveMessage("Game", player.getName() + " left.");
                player.delete();
            }
            else if (object instanceof Networking.TaskFinished) {
                Networking.TaskFinished update = ((Networking.TaskFinished) object);
                try {
                    Task task = ((Task) GameObject.objects.get(update.taskID));
                    task.setComplete(true);
                    task.setFailed(update.failed);
                }
                catch (NullPointerException | ClassCastException ignored) {}
            }
            else if (object instanceof Networking.StartGame) {
                Networking.StartGame update = ((Networking.StartGame) object);
                for (int i = 0; i < update.playerInfos.length; i++) {
                    if (GameObject.objects.get(i) instanceof Player) {
                        Player player = ((Player) GameObject.objects.get(i));
                        player.setSaboteur(update.playerInfos[i].isSaboteur);

                        if (player == game.getMainPlayer()) {
                            String message = "You are a duck...";
                            if (player.isSaboteur())
                                message = "You are a saboteur...";
                            game.getHud().getChat().receiveMessage("Game", message);
                        }
                    }
                }
                game.getRandom().setSeed(update.seed);

                if (game.getScreen() instanceof GameScreen) {
                    ((GameScreen) game.getScreen()).setStartGameFlag(true, update.playerInfos, update.taskInfos, update.itemInfos);
                }

            }
            else if (object instanceof Networking.EndGame) {
                Networking.EndGame update = ((Networking.EndGame) object);
                game.showEndScreen(update.saboteursWin && game.mainPlayer.isSaboteur() || !update.saboteursWin && !game.mainPlayer.isSaboteur());
            }
            else if (object instanceof Networking.TextMessage) {
                Networking.TextMessage update = ((Networking.TextMessage) object);
                synchronized (game.getHud()) {
                    game.getHud().receiveMessage(update.sender, update.message);
                }
            }
        }

        @Override
        public void connected(Connection connection) {
            connection.sendTCP(Networking.joinRequest(game.customisation, password));
        }

        @Override
        public void disconnected(Connection connection) {
            game.showErrorMessage("Disconnected");
        }
    }

    private SocialGame game;

    public GameClient(SocialGame game, String password) throws IOException {
        this(game, password, "localhost");
    }

    public GameClient(SocialGame game, String password, String host) throws IOException {
        this(game, password, host, Networking.TCP_PORT, Networking.UDP_PORT);
    }

    public GameClient(SocialGame game, String password, String host, int tcpPort, int udpPort) throws IOException {
        super();

        // Register networking classes
        Networking.register(this);

        addListener(new GameClientListener(game, password));
        start();
        connect(5000, host, tcpPort, udpPort);
    }
}
