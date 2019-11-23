package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class ResLoginMsg extends AMessage {
    private String phone;
    private String vcode;
    public ResLoginMsg(String phone,String code) {
        super(IMessage.REQ_LOGIN_RS);
        this.phone = phone;
        this.vcode = code;
    }

    public String getVcode() {
        return vcode;
    }

    public String getPhone() {
        return phone;
    }
}
