package com.socialgame.game.networking.voicechat;

public class VoiceNetData {

    private short[] data;

    public VoiceNetData(){

    }

    public VoiceNetData(short[] data){
        this.data = data;
    }

    public short[] getData(){
        return data;
    }
}
