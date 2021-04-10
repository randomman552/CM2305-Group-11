package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer extends Server {
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54556;

    public GameServer() throws IOException {
        this(TCP_PORT, UDP_PORT);
    }

    public GameServer(int TCPPort, int UDPPort) throws IOException {
        super();
        start();
        bind(TCPPort, UDPPort);

        // Register event types

        addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                Connection[] connections = getConnections();
                for (Connection con: connections) {
                    con.sendTCP(object);
                }
            }
        });
    }
}
