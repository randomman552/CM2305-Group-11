package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.socialgame.game.GameCoordinator;
import com.socialgame.game.player.PlayerCustomisation;

import java.io.IOException;

public class GameServer extends Server {
    public static final int MAX_PLAYERS = 16;

    /**
     * Array of information about players.
     */
    private final Networking.PlayerInfo[] connectedPlayers = new Networking.PlayerInfo[MAX_PLAYERS];
    private final GameCoordinator coordinator = new GameCoordinator();

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
                // Check password
                if (!update.password.equals(server.password)) {
                    return;
                }

                // Attempt to add player
                int playerID = server.addPlayer(connection.getID(), update.getCustomisation());

                // Refuse if server full
                if (playerID == -1) {
                    connection.sendTCP(Networking.joinRefused("Server is full"));
                    return;
                }
                server.sendToAllTCP(Networking.joinAccepted(server.connectedPlayers, playerID));
            }
            else if (object instanceof Networking.PositionUpdate) {
                Networking.PositionUpdate update = ((Networking.PositionUpdate) object);
                Networking.PlayerInfo info = server.connectedPlayers[server.getPlayerID(connection.getID())];
                info.x = update.x;
                info.y = update.y;
                server.sendToAllExceptTCP(connection.getID(), object);
            }
            else if (object instanceof Networking.InitialiseGame) {
                int[] saboteurIDs = server.coordinator.pickSaboteurs(server.getNumPlayers());
                for (int i: saboteurIDs) {
                    server.setSaboteur(i);
                }
                server.sendToAllTCP(Networking.startGame(server.connectedPlayers));
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

    private final String password;

    public GameServer(String password) throws IOException {
        this(password, Networking.TCP_PORT, Networking.UDP_PORT);
    }

    public GameServer(String password, int TCPPort, int UDPPort) throws IOException {
        super();
        this.password = password;

        // Register networking classes
        Networking.register(this);

        addListener(new GameServerListener(this));

        start();
        bind(TCPPort, UDPPort);
    }

    public int getNumPlayers() {
        int count = 0;
        for (Networking.PlayerInfo connectedPlayer : connectedPlayers) {
            if (connectedPlayer != null) count++;
        }
        return count;
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

        // Remove player if present
        if (i != -1) {
            connectedPlayers[i] = null;
            sendToAllTCP(Networking.leaveNotification(i));
        }
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

    /**
     * Set the player with the given ID as a saboteur, if they are connected.
     * @param playerID The ID of the player.
     */
    public void setSaboteur(int playerID) {
        if (connectedPlayers[playerID] != null) {
            connectedPlayers[playerID].isSaboteur = true;
        }
    }

    @Override
    public void close() {
        // On close, remove all players from the game
        for (Connection conn: getConnections()) {
            removePlayer(conn.getID());
        }
        super.close();
    }
}
