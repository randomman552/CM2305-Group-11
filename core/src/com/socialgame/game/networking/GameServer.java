package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer extends Server {
    private static final int MAX_PLAYERS = 16;

    public int players = 0;

    private static class GameServerListener extends Listener {
        private final GameServer server;

        public GameServerListener(GameServer server) {
            this.server = server;
        }

        @Override
        public void received(Connection connection, Object object) {
            System.out.println("Server Receives: " + object);

            if (object instanceof Networking.JoinRequest && server.players != GameServer.MAX_PLAYERS) {
                server.sendToAllTCP(Networking.joinResponse(0, server.players++, 0, 0));
            } else {
                server.sendToAllExceptTCP(connection.getID(), object);
            }
        }
    }

    public GameServer() throws IOException {
        this(Networking.TCP_PORT, Networking.UDP_PORT);
    }

    public GameServer(int TCPPort, int UDPPort) throws IOException {
        super();

        // Register networking classes
        Networking.register(this);

        addListener(new GameServerListener(this));

        start();
        bind(TCPPort, UDPPort);
    }
}
