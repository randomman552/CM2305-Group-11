package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.player.Player;

import java.io.IOException;

public class GameClient extends Client {
    public static class GameClientListener extends Listener {
        private final GameClient client;

        public GameClientListener(GameClient client) {
            this.client = client;
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
            else if (object instanceof  Networking.DropItemUpdate) {
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
        }
    }

    public GameClient() throws IOException {
        this("localhost");
    }

    public GameClient(String host) throws IOException {
        this(host, Networking.TCP_PORT, Networking.UDP_PORT);
    }

    public GameClient(String host, int tcpPort, int udpPort) throws IOException {
        super();

        start();
        connect(5000, host, tcpPort, udpPort);

        // Register networking classes
        Networking.register(this);

        addListener(new GameClientListener(this));
    }
}
