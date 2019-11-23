package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

import java.io.Serializable;

public class AMessage implements IMessage, Serializable {
    private int MsgType;
    public AMessage(int MsgType){
        this.MsgType = MsgType;
    }
    @Override
    public int getMsgType() {
        return this.MsgType;
    }
}
