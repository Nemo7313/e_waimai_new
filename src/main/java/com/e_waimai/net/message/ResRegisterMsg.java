package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class ResRegisterMsg extends AMessage {
    private String name;
    private String address;
    private String introduction;
    public ResRegisterMsg(String name, String addr, String intro) {
        super(IMessage.RS_REG);
        this.name = name;
        this.address = addr;
        this.introduction = intro;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getIntroduction() {
        return introduction;
    }
}
