package com.socialgame.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.socialgame.game.util.customisation.Customisation;

import java.io.IOException;

public class GameServer extends Server {
    public static final int MAX_PLAYERS = 16;
    private static final int MIN_PLAYERS = 1;

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
                    connection.sendTCP(Networking.joinRefused("Incorrect password"));
                    return;
                }

                // Check if game has already started
                if (server.coordinator.isStarted()) {
                    connection.sendTCP(Networking.joinRefused("Game has already started"));
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
                if (connection.getRemoteAddressTCP().toString().split(":")[0].equals("/127.0.0.1")) {
                    if (server.getNumPlayers() >= MIN_PLAYERS) {
                        server.initialiseGame();
                        server.sendToAllTCP(Networking.startGame(server.connectedPlayers, server.tasks, server.items, server.getSeed()));
                        server.coordinator.setStarted(true);
                        // Start hazard timer
                        new GameCoordinator.HazardTimerThread(server).start();
                    }
                }
            }
            else if (object instanceof Networking.TaskFinished) {
                Networking.TaskFinished update = ((Networking.TaskFinished) object);
                Networking.TaskInfo taskInfo = server.getTask(update.taskID);
                taskInfo.completed = true;
                taskInfo.failed = update.failed;
                server.sendToAllExceptTCP(connection.getID(), object);
                server.checkWinConditions();
            }
            else if (object instanceof Networking.PlayerTakeDamageUpdate) {
                Networking.PlayerTakeDamageUpdate update = ((Networking.PlayerTakeDamageUpdate) object);
                Networking.PlayerInfo playerInfo = server.connectedPlayers[update.playerID];
                playerInfo.isAlive = false;
                server.sendToAllExceptTCP(connection.getID(), object);
                server.checkWinConditions();
            }
            else if (object instanceof Networking.TextMessage) {
                server.sendToAllTCP(object);
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

    private final Networking.PlayerInfo[] connectedPlayers = new Networking.PlayerInfo[MAX_PLAYERS];
    private Networking.TaskInfo[] tasks = new Networking.TaskInfo[0];
    private Networking.ItemInfo[] items = new Networking.ItemInfo[0];
    private final GameCoordinator coordinator = new GameCoordinator();

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

    public int getNumSaboteurs() {
        int num = 0;
        for (Networking.PlayerInfo player: connectedPlayers) {
            if (player != null && player.isSaboteur) num++;
        }
        return num;
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
            sendToAllTCP(Networking.textMessage("Server", "Player " + i + " left."));
        }
    }

    /**
     * Method to add a new player to the server
     * @param connectionID The ID of the connection requesting to join
     * @param customisation The customisation object of the player
     * @return The new players id if added, -1 otherwise
     */
    public int addPlayer(int connectionID, Customisation customisation) {
        for (int i = 0; i < connectedPlayers.length; i++) {
            if (connectedPlayers[i] == null) {
                connectedPlayers[i] = new Networking.PlayerInfo(connectionID, customisation);
                sendToAllTCP(Networking.textMessage("Server", "Player " + i + " joined."));
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


    public Networking.TaskInfo getTask(int id) {
        for (Networking.TaskInfo taskInfo: tasks) {
            if (taskInfo != null && taskInfo.id == id) {
                return taskInfo;
            }
        }
        return null;
    }

    public int getTasksComplete() {
        int num = 0;
        for (Networking.TaskInfo task: tasks) {
            if (task.completed) num++;
        }
        return num;
    }

    public int getTasksTodo() {
        return  tasks.length - getTasksComplete();
    }


    /**
     * Get the seed to communicate to clients to use as the map seed.
     * @return random seed for map generation.
     */
    public long getSeed() {
        return coordinator.getSeed();
    }

    private void initialiseGame() {
        int[] saboteurIDs = coordinator.pickSaboteurs(getNumPlayers());
        for (int i : saboteurIDs) {
            setSaboteur(i);
        }
        tasks = coordinator.generateTasks(getNumPlayers());
        items = coordinator.generateItems(getNumPlayers());
        sendToAllTCP(Networking.textMessage("Server", "game begins."));
    }


    private void checkWinConditions() {
        // Check if saboteurs win
        if (getNumPlayers() <= getNumSaboteurs() || coordinator.checkHazardWinCondition()) sendToAllTCP(Networking.endGame(true));

        // Check if players win
        if (getTasksTodo() <= 0) sendToAllTCP(Networking.endGame(false));
    }

    protected void hazardAdvance (float timeStep) {
        coordinator.incrementHazard(timeStep);
        sendToAllTCP(Networking.hazardAdvance(coordinator.getHazardValue()));
        checkWinConditions();
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
