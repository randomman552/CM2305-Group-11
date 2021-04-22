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
            server.sendToAllExceptTCP(connection.getID(), object);
        }

        @Override
        public void connected(Connection connection) {
            connection.sendTCP(Networking.joinResponse(server.players++, 0, 0, 0));
        }

        @Override
        public void disconnected(Connection connection) {
            // FIXME: 22/04/2021 This doesn't account for reshuffling players to allow a player to replace one that has left (players cannot rejoin)
            server.sendToAllTCP(Networking.leaveNotification(connection.getID() - 1));
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
