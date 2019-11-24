package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class CusRegisterMsg extends AMessage {
    private String name;
    private String address;

    public CusRegisterMsg(String name, String addr) {
        super(IMessage.CS_REG);
        this.name = name;
        this.address = addr;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
