package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class CusVcodeMsg extends AMessage {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public CusVcodeMsg(String phone) {
        super(IMessage.REQ_VCODE_CUS);
        this.phone = phone;
    }
}
