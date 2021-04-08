package com.socialgame.game.networking;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.socialgame.game.networking.updates.PositionChangeEvent;

import java.io.IOException;

public class GameClient extends Client {
    public GameClient(Stage stage) throws IOException {
        this(GameServer.TCP_PORT, GameServer.UDP_PORT, "localhost", stage);
    }

    public GameClient(int TCPPort, int UDPPort, String host, final Stage stage) throws IOException {
        super();
        start();
        connect(5000, host, TCPPort, UDPPort);

        // Register event types
        getKryo().register(PositionChangeEvent.class);

        addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Event)
                    stage.getRoot().fire(((Event) object));
            }
        });
    }
}
