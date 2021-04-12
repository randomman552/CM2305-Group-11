package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.socialgame.game.baseclasses.GameObject;

import java.io.IOException;

public class GameClient extends Client {
    public static class GameClientListener extends Listener {
        private final GameClient client;

        public GameClientListener(GameClient client) {
            this.client = client;
        }

        @Override
        public void received(Connection connection, Object object) {
            System.out.println("Client Receives: " + object);
            if (object instanceof Networking.PositionUpdate) {
                Networking.PositionUpdate update = ((Networking.PositionUpdate) object);
                GameObject obj = GameObject.objects.get(update.id);
                obj.setPosition(update.x, update.y);
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
