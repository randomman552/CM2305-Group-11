package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.player.Player;

import java.io.IOException;

public class GameClient extends Client {
    public static class GameClientListener extends Listener {
        private final SocialGame game;

        public GameClientListener(SocialGame game) {
            this.game = game;
        }

        @Override
        public void received(Connection connection, Object object) {
            // FIXME: 19/04/2021 Clean this up if possible
            System.out.println("Client Receives: " + object);
            if (object instanceof Networking.VelocityUpdate) {
                Networking.VelocityUpdate update = ((Networking.VelocityUpdate) object);
                GameObject obj = GameObject.objects.get(update.id);
                obj.body.setLinearVelocity(update.xVel, update.yVel);
            }
            else if (object instanceof Networking.PositionUpdate) {
                Networking.PositionUpdate update = ((Networking.PositionUpdate) object);
                GameObject obj = GameObject.objects.get(update.id);
                obj.setPositionAboutOrigin(update.x, update.y);
            }
            else if (object instanceof Networking.PickupItemUpdate) {
                Networking.PickupItemUpdate update = ((Networking.PickupItemUpdate) object);
                GameObject player = GameObject.objects.get(update.playerID);
                Interactable item = (Interactable) GameObject.objects.get(update.itemID);
                item.interact(player);
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
            else if (object instanceof Networking.PlayerTakeDamageUpdate) {
                Networking.PlayerTakeDamageUpdate update = (Networking.PlayerTakeDamageUpdate) object;
                Player player = (Player) GameObject.objects.get(update.playerID);
                player.takeDamage(update.damage);
            }
            else if (object instanceof Networking.JoinResponse) {
                Networking.JoinResponse update = (Networking.JoinResponse) object;
                // Create a new player in response to a player joining the game
                Player newPlayer = new Player(game, update.playerID);
                game.getMainStage().addActor(newPlayer);
                newPlayer.setPositionAboutOrigin(update.x, update.y);

                // If our game.mainPlayer is null, this is the response to our join request
                if (game.mainPlayer == null) {
                    game.mainPlayer = newPlayer;
                    for (int i = update.playerID - 1; i >= 0; i--) {
                        game.getMainStage().addActor(new Player(game, i));
                    }
                } else {
                    // Otherwise we must inform the new player of our position
                    int id = game.mainPlayer.getId();
                    float x = game.mainPlayer.getX();
                    float y = game.mainPlayer.getY();
                    connection.sendTCP(Networking.positionUpdate(id, x, y));
                }
            }
            else if (object instanceof Networking.LeaveNotification) {
                Networking.LeaveNotification update = ((Networking.LeaveNotification) object);
                GameObject player = GameObject.objects.get(update.playerID);
                player.delete();
            }
        }
    }

    public SocialGame game;

    public GameClient(SocialGame game) throws IOException {
        this(game, "localhost");
    }

    public GameClient(SocialGame game, String host) throws IOException {
        this(game, host, Networking.TCP_PORT, Networking.UDP_PORT);
    }

    public GameClient(SocialGame game, String host, int tcpPort, int udpPort) throws IOException {
        super();

        // Register networking classes
        Networking.register(this);

        addListener(new GameClientListener(game));
        start();
        connect(5000, host, tcpPort, udpPort);
    }
}
