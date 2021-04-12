package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer extends Server {
    private static class GameServerListener extends Listener {
        private final GameServer server;

        public GameServerListener(GameServer server) {
            this.server = server;
        }

        @Override
        public void received(Connection connection, Object object) {
            System.out.println("Server Receives: " + object);
            if (object instanceof Networking.PositionUpdate) {
                server.sendToAllExceptTCP(connection.getID(), object);
            }
        }
    }

    public GameServer() throws IOException {
        this(Networking.TCP_PORT, Networking.UDP_PORT);
    }

    public GameServer(int TCPPort, int UDPPort) throws IOException {
        super();
        start();
        bind(TCPPort, UDPPort);

        // Register networking classes
        Networking.register(this);

        addListener(new GameServerListener(this));
    }
}
