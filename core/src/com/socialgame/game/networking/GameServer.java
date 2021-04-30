package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.socialgame.game.player.PlayerCustomisation;

import java.io.IOException;

public class GameServer extends Server {
    public static final int MAX_PLAYERS = 16;

    /**
     * Array of information about players.
     */
    private final Networking.PlayerInfo[] connectedPlayers = new Networking.PlayerInfo[MAX_PLAYERS];

    private static class GameServerListener extends Listener {
        private final GameServer server;

        public GameServerListener(GameServer server) {
            this.server = server;
        }

        @Override
        public void received(Connection connection, Object object) {
            System.out.println("Server Receives: " + object);
            if (object instanceof Networking.JoinRequest) {
                Networking.JoinRequest update = ((Networking.JoinRequest) object);
                int playerID = server.addPlayer(connection.getID(), update.getCustomisation());

                // Return appropriate response to join request
                if (playerID == -1) {
                    connection.sendTCP(Networking.joinRefused("Server is full"));
                    return;
                }
                server.sendToAllTCP(Networking.joinAccepted(server.connectedPlayers, playerID));
            }
            else {
                server.sendToAllExceptTCP(connection.getID(), object);
            }
        }

        @Override
        public void disconnected(Connection connection) {
            server.removePlayer(connection.getID());
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

    /**
     * Get the player id of the player with the given connection ID
     * @param connectionID The connection ID to check for
     * @return The id of the player with the given connection id
     */
    public int getPlayerID(int connectionID) {
        for (int i = 0; i < connectedPlayers.length; i++)
            if (connectedPlayers[i] != null && connectedPlayers[i].connectionID == connectionID)
                return i;
        return -1;
    }

    /**
     * Remove a player on their disconnecting from the server
     * @param connectionID The connection id of the leaving player
     */
    public void removePlayer(int connectionID) {
        int i = getPlayerID(connectionID);
        sendToAllTCP(Networking.leaveNotification(i));
        connectedPlayers[i] = null;
    }

    /**
     * Method to add a new player to the server
     * @param connectionID The ID of the connection requesting to join
     * @param customisation The customisation object of the player
     * @return The new players id if added, -1 otherwise
     */
    public int addPlayer(int connectionID, PlayerCustomisation customisation) {
        for (int i = 0; i < connectedPlayers.length; i++) {
            if (connectedPlayers[i] == null) {
                connectedPlayers[i] = new Networking.PlayerInfo(connectionID, customisation);
                return i;
            }
        }
        return -1;
    }
}
