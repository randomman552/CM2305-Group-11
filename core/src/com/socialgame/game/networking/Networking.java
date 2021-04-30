package com.socialgame.game.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.socialgame.game.player.PlayerCustomisation;

import java.util.Arrays;

public class Networking {
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54556;
    public static final int TICK_RATE = 32;

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

        kryo.register(PlayerInfo[].class);
        kryo.register(PlayerInfo.class);
        kryo.register(int[].class);

        kryo.register(PositionUpdate.class);
        kryo.register(VelocityUpdate.class);
        kryo.register(PickupItemUpdate.class);
        kryo.register(DropItemUpdate.class);
        kryo.register(SwitchItemUpdate.class);
        kryo.register(PlayerTakeDamageUpdate.class);
        kryo.register(LeaveNotification.class);
        kryo.register(JoinRequest.class);
        kryo.register(JoinAccepted.class);
    }

    static public void resetPools() {

    }

    /**
     * Class used to convey player information between clients
     */
    public static class PlayerInfo {
        public int connectionID;
        public int hatSelection;
        public int topSelection;
        public int colorSelection;
        public int[] inventory = new int[2];
        public float x, y;

        public PlayerInfo() {

        }

        public PlayerInfo(int connectionID, PlayerCustomisation customisation) {
            this(connectionID, customisation, 0, 0);
        }

        public PlayerInfo(int connectionID, PlayerCustomisation customisation, float x, float y) {
            this.connectionID = connectionID;
            this.hatSelection = customisation.getHatSelection();
            this.topSelection = customisation.getTopSelection();
            this.colorSelection = customisation.getColorSelection();
            this.x = x;
            this.y = y;
        }

        public PlayerCustomisation getCustomisation() {
            PlayerCustomisation customisation = new PlayerCustomisation();
            customisation.setHatSelection(hatSelection);
            customisation.setTopSelection(topSelection);
            customisation.setColorSelection(colorSelection);
            return customisation;
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

    // region Network classes pool access methods

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

    // region Initial connection classes

    public static JoinRequest joinRequest(PlayerCustomisation customisation, String password) {
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

    // endregion

    // region Network classes

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

    // region Initial connection classes

    public static class JoinRequest {
        public int hatSelection;
        public int topSelection;
        public int colorSelection;
        public String password;

        public PlayerCustomisation getCustomisation() {
            PlayerCustomisation customisation = new PlayerCustomisation();
            customisation.setHatSelection(hatSelection);
            customisation.setTopSelection(topSelection);
            customisation.setColorSelection(colorSelection);
            return customisation;
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

    // endregion
}
