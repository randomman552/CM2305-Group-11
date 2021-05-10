package com.socialgame.game.networking.voicechat;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.socialgame.game.networking.Networking;

import java.io.IOException;

public class VoiceChatServer extends Server {
    private final VoiceServer voiceServer;

    private static class VoiceChatServerListener extends Listener {
        private final VoiceChatServer server;

        public VoiceChatServerListener(VoiceChatServer server) {
            this.server = server;
        }

        @Override
        public void received(Connection connection, Object object) {
            super.received(connection, object);
            System.out.println("VoiceChatServer Receives: " + object);
        }
    }
    public VoiceChatServer()throws IOException {
        this(Networking.TCPVC_PORT, Networking.UDPVC_PORT);
    }
    public VoiceChatServer(int TCP, int UDP) throws IOException {
        super(22050, 22050);
        Networking.register(this);
        addListener(new VoiceChatServerListener(this));
        bind(TCP, UDP);
        start();

        // Now make voice chat server.
        voiceServer = new VoiceServer(getKryo());

         //Now we need to enable the server to transmit audio data.
        final VoiceChatServer server = this;
        addListener(new Listener() {
            public void received(Connection connection, Object object) {
                // This 'bounces' back any audio data sent from clients.
                voiceServer.relayVoice(connection, object, server);
            }
        });


    }
}
