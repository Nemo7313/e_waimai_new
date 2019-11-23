package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class VCodeMsg extends AMessage {
    private String phone;

    public VCodeMsg(String phone) {
        super(IMessage.REQ_VCODE);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
