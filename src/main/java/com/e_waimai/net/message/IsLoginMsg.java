package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class IsLoginMsg extends AMessage{
    boolean flag;
    public IsLoginMsg(boolean flag) {
        super(IMessage.BOOLEAN_MSG);
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }
}
