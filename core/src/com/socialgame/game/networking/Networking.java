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

    // endregion

    // region Network classes

    public static class PositionUpdate {
        public int id;
        public float x, y;
    }

    // endregion
}
