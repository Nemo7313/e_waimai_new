package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class TextMsg extends AMessage{
    private String message;
    public TextMsg(String msg){
        super(IMessage.TEXT_MSG);
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
