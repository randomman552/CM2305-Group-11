package com.socialgame.game.networking.voicechat;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.socialgame.game.SocialGame;
import com.socialgame.game.networking.Networking;

import java.io.IOException;

public class VoiceChatClient extends Client {
    private SocialGame game;


    public static class VoiceChatListener extends Listener {

        private final SocialGame game;

        public VoiceChatListener(SocialGame game) {
            this.game = game;
        }

        @Override
        public void received(Connection connection, Object object) {
            super.received(connection, object);
            System.out.println("Client Receives: " + object);
        }
    }
    public VoiceChatClient(String host) throws IOException {
        this(host, Networking.TCPVC_PORT, Networking.UDPVC_PORT);
    }

    public VoiceChatClient(String host, int TCPVC_PORT, int UDPVC_PORT) throws IOException {
        super(44100, 44100);
        Networking.register(this);

        addListener(new VoiceChatListener(game));
        start();
        connect(5000, host, TCPVC_PORT, UDPVC_PORT);
    }



}
