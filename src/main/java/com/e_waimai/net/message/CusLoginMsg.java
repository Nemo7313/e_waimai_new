package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class CusLoginMsg extends  AMessage{
    private String phone;
    private String vcode;

    public String getPhone() {
        return phone;
    }

    public String getVcode() {
        return vcode;
    }

    public CusLoginMsg(String phone, String vCode) {
        super(IMessage.REQ_LOGIN_CS);
        this.phone = phone;
        this.vcode = vCode;
    }
}
