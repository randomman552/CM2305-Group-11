package com.socialgame.game.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.socialgame.game.networking.voicechat.VoiceNetData;
import com.socialgame.game.util.customisation.Customisation;
import com.socialgame.game.util.customisation.UnlinkedCustomisation;

import java.util.Arrays;

public class Networking {
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54556;
    public static final int TCPVC_PORT = 54557;
    public static final int UDPVC_PORT = 54558;

    // Setup initial pools on class init
    static {
        resetPools();
    }

    /**
     * Method to register objects that are going to be send over the network
     * @param endPoint The endpoint to register for
     */
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        // Register helper classes
        kryo.register(PlayerInfo[].class);
        kryo.register(PlayerInfo.class);
        kryo.register(ItemInfo[].class);
        kryo.register(ItemInfo.class);
        kryo.register(TaskInfo[].class);
        kryo.register(TaskInfo.class);
        kryo.register(int[].class);
        kryo.register(short[].class);

        // Register update classes
        kryo.register(PositionUpdate.class);
        kryo.register(VelocityUpdate.class);
        kryo.register(PickupItemUpdate.class);
        kryo.register(DropItemUpdate.class);
        kryo.register(SwitchItemUpdate.class);
        kryo.register(PlayerTakeDamageUpdate.class);
        kryo.register(LeaveNotification.class);
        kryo.register(JoinRequest.class);
        kryo.register(JoinAccepted.class);
        kryo.register(JoinRefused.class);
        kryo.register(TaskFinished.class);
        kryo.register(InitialiseGame.class);
        kryo.register(StartGame.class);
        kryo.register(EndGame.class);
        kryo.register(VoiceNetData.class);
        kryo.register(TextMessage.class);
    }

    static public void resetPools() {

    }

    /**
     * Class used to convey player information between clients
     */
    public static class PlayerInfo {
        public int connectionID = 0;
        public int hatSelection = 0;
        public int topSelection = 0;
        public int colorSelection = 0;
        public ItemInfo[] inventory = new ItemInfo[2];
        public boolean isSaboteur, isAlive = false;
        public float x, y = 0;

        public PlayerInfo() {

        }

        public PlayerInfo(int connectionID, Customisation customisation) {
            this(connectionID, customisation, 0, 0);
        }

        public PlayerInfo(int connectionID, Customisation customisation, float x, float y) {
            this.connectionID = connectionID;
            this.hatSelection = customisation.getHatSelection();
            this.topSelection = customisation.getTopSelection();
            this.colorSelection = customisation.getColorSelection();
            this.isSaboteur = false;
            this.x = x;
            this.y = y;
        }

        public Customisation getCustomisation() {
            return new UnlinkedCustomisation(colorSelection, hatSelection, topSelection);
        }

        @Override
        public String toString() {
            return "PlayerInfo{" +
                    "connectionID=" + connectionID +
                    ", hatSelection=" + hatSelection +
                    ", topSelection=" + topSelection +
                    ", colorSelection=" + colorSelection +
                    ", inventory=" + Arrays.toString(inventory) +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    /**
     * Class used to convey item information between clients.
     */
    public static class ItemInfo {
        public int id, type = 0;

        @Override
        public String toString() {
            return "ItemInfo{" +
                    "id=" + id +
                    ", type=" + type +
                    '}';
        }
    }

    /**
     * Class used to convey task information between clients.
     */
    public static class TaskInfo {
        public int id, type = 0;
        public boolean completed, failed = false;

        @Override
        public String toString() {
            return "TaskInfo{" +
                    "id=" + id +
                    ", type=" + type +
                    '}';
        }
    }

    // region Network classes access methods

    // region Player updates

    public static PositionUpdate positionUpdate(int id, float x, float y) {
        PositionUpdate obj = new PositionUpdate();
        obj.id = id;
        obj.x = x;
        obj.y = y;
        return obj;
    }

    public static VelocityUpdate velocityUpdate(int id, float xVel, float yVel) {
        VelocityUpdate obj = new VelocityUpdate();
        obj.id = id;
        obj.xVel = xVel;
        obj.yVel = yVel;
        return obj;
    }

    public static PickupItemUpdate pickupItemUpdate(int playerID, int itemID) {
        PickupItemUpdate obj = new PickupItemUpdate();
        obj.playerID = playerID;
        obj.itemID = itemID;
        return obj;
    }

    public static DropItemUpdate dropItemUpdate(int playerID) {
        DropItemUpdate obj = new DropItemUpdate();
        obj.playerID = playerID;
        return obj;
    }

    public static SwitchItemUpdate switchItemUpdate(int playerID, int newSlot) {
        SwitchItemUpdate obj = new SwitchItemUpdate();
        obj.playerID = playerID;
        obj.newSlot = newSlot;
        return obj;
    }

    public static PlayerTakeDamageUpdate playerTakeDamageUpdate(int playerID, float damage) {
        PlayerTakeDamageUpdate obj = new PlayerTakeDamageUpdate();
        obj.playerID = playerID;
        obj.damage = damage;
        return obj;
    }

    // endregion

    // region Initial connection classes

    public static JoinRequest joinRequest(Customisation customisation, String password) {
        JoinRequest obj = new JoinRequest();
        obj.hatSelection = customisation.getHatSelection();
        obj.topSelection = customisation.getTopSelection();
        obj.colorSelection = customisation.getColorSelection();
        obj.password = password;
        return obj;
    }

    public static JoinRefused joinRefused(String reason) {
        JoinRefused obj = new JoinRefused();
        obj.reason = reason;
        return obj;
    }

    public static JoinAccepted joinAccepted(PlayerInfo[] playerInfos, int playerID) {
        JoinAccepted obj = new JoinAccepted();
        obj.playerInfos = playerInfos;
        obj.playerID = playerID;
        return obj;
    }

    public static LeaveNotification leaveNotification(int playerID) {
        LeaveNotification obj = new LeaveNotification();
        obj.playerID = playerID;
        return obj;
    }

    // endregion

    // region GameCoordinator updates

    public static InitialiseGame initialiseGame() {
        return new InitialiseGame();
    }

    public static StartGame startGame(PlayerInfo[] playerInfos, TaskInfo[] taskInfos, ItemInfo[] itemInfos, long mapSeed) {
        StartGame obj = new StartGame();
        obj.playerInfos = playerInfos;
        obj.taskInfos = taskInfos;
        obj.itemInfos = itemInfos;
        obj.seed = mapSeed;
        return obj;
    }

    public static EndGame endGame(boolean saboteursWin) {
        EndGame obj = new EndGame();
        obj.saboteursWin = saboteursWin;
        return obj;
    }

    // endregion

    // region Task sync updates

    public static TaskFinished taskFinished(int taskID, boolean failed) {
        TaskFinished obj = new TaskFinished();
        obj.taskID = taskID;
        obj.failed = failed;
        return obj;
    }

    public static TaskFinished taskFinished(int taskID) {
        return taskFinished(taskID, false);
    }

    // endregion

    // region Text Chat

    public static TextMessage textMessage(String sender, String message) {
        TextMessage obj = new TextMessage();
        obj.sender = sender;
        obj.message = message;
        return obj;
    }

    // endregion

    // endregion

    // region Network classes

    // region Player updates

    public static class PositionUpdate {
        public int id;
        public float x, y;

        @Override
        public String toString() {
            return "PositionUpdate{" +
                    "id=" + id +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static class VelocityUpdate {
        public int id;
        public float xVel, yVel;

        @Override
        public String toString() {
            return "VelocityUpdate{" +
                    "id=" + id +
                    ", xVel=" + xVel +
                    ", yVel=" + yVel +
                    '}';
        }
    }

    public static class PickupItemUpdate {
        public int playerID;
        public int itemID;

        @Override
        public String toString() {
            return "PickupItemUpdate{" +
                    "playerID=" + playerID +
                    ", itemID=" + itemID +
                    '}';
        }
    }

    public static class DropItemUpdate {
        public int playerID;

        @Override
        public String toString() {
            return "DropItemUpdate{" +
                    "playerID=" + playerID +
                    '}';
        }
    }

    public static class SwitchItemUpdate {
        public int playerID;
        public int newSlot;

        @Override
        public String toString() {
            return "SwitchItemUpdate{" +
                    "playerID=" + playerID +
                    ", newSlot=" + newSlot +
                    '}';
        }
    }

    public static class PlayerTakeDamageUpdate {
        public int playerID;
        public float damage;

        @Override
        public String toString() {
            return "PlayerTakeDamageUpdate{" +
                    "playerID=" + playerID +
                    ", damage=" + damage +
                    '}';
        }
    }

    // endregion

    // region Initial connection classes

    public static class JoinRequest {
        public int hatSelection;
        public int topSelection;
        public int colorSelection;
        public String password;

        public Customisation getCustomisation() {
            return new UnlinkedCustomisation(colorSelection, hatSelection, topSelection);
        }

        @Override
        public String toString() {
            return "JoinRequest{" +
                    "hatSelection=" + hatSelection +
                    ", topSelection=" + topSelection +
                    ", colorSelection=" + colorSelection +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static class JoinRefused {
        public String reason;

        @Override
        public String toString() {
            return "JoinRefused{" +
                    "reason='" + reason + '\'' +
                    '}';
        }
    }

    public static class JoinAccepted {
        public PlayerInfo[] playerInfos = new PlayerInfo[GameServer.MAX_PLAYERS];
        public int playerID;

        @Override
        public String toString() {
            return "JoinAccepted{" +
                    "playerInfos=" + Arrays.toString(playerInfos) +
                    ", playerID=" + playerID +
                    '}';
        }
    }

    public static class LeaveNotification {
        public int playerID;

        @Override
        public String toString() {
            return "LeaveNotification{" +
                    "playerID=" + playerID +
                    '}';
        }
    }

    //endregion

    // region GameCoordinator updates

    /**
     * Class sent by clients to instruct server to start the game.
     */
    public static class InitialiseGame {
        @Override
        public String toString() {
            return "InitialiseGame{}";
        }
    }

    /**
     * Class sent by server to synchronise clients on game start.
     */
    public static class StartGame {
        public TaskInfo[] taskInfos = new TaskInfo[0];
        public ItemInfo[] itemInfos = new ItemInfo[0];
        PlayerInfo[] playerInfos = new PlayerInfo[0];
        long seed;

        @Override
        public String toString() {
            return "StartGame{" +
                    "taskInfos=" + Arrays.toString(taskInfos) +
                    ", itemInfos=" + Arrays.toString(itemInfos) +
                    ", playerInfos=" + Arrays.toString(playerInfos) +
                    ", seed=" + seed +
                    '}';
        }
    }

    /**
     * Class send by server to end a game.
     */
    public static class EndGame {
        public boolean saboteursWin;

        @Override
        public String toString() {
            return "EndGame{" +
                    "saboteursWin=" + saboteursWin +
                    '}';
        }
    }

    // endregion

    // region Task sync updates

    public static class TaskFinished {
        public int taskID;
        public boolean failed;

        @Override
        public String toString() {
            return "TaskFinished{" +
                    "taskID=" + taskID +
                    ", failed=" + failed +
                    '}';
        }
    }

    // endregion

    // region Text Chat

    public static class TextMessage {
        public String sender;
        public String message;

        @Override
        public String toString() {
            return "TextMessage{" +
                    "sender='" + sender + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    // endregion

    // endregion
}
