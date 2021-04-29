package com.socialgame.game.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

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
        kryo.register(PositionUpdate.class);
        kryo.register(VelocityUpdate.class);
        kryo.register(PickupItemUpdate.class);
        kryo.register(DropItemUpdate.class);
        kryo.register(SwitchItemUpdate.class);
        kryo.register(PlayerTakeDamageUpdate.class);
        kryo.register(JoinResponse.class);
        kryo.register(LeaveNotification.class);
    }

    static public void resetPools() {

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

    public static JoinResponse joinResponse(int playerID, float x, float y, int mapSeed) {
        JoinResponse obj = new JoinResponse();
        obj.mapSeed = mapSeed;
        obj.playerID = playerID;
        obj.x = x;
        obj.y = y;
        return obj;
    }

    public static JoinResponse joinResponse(int playerID, float x, float y) {
        return joinResponse(playerID, x, y, 0);
    }

    public static LeaveNotification leaveNotification(int playerID) {
        LeaveNotification obj = new LeaveNotification();
        obj.playerID = playerID;
        return obj;
    }

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

    public static class JoinResponse {
        public int mapSeed;
        public int playerID;
        public float x;
        public float y;

        @Override
        public String toString() {
            return "JoinResponse{" +
                    "mapSeed=" + mapSeed +
                    ", playerID=" + playerID +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static class LeaveNotification {
        public int playerID;
    }

    // endregion
}
