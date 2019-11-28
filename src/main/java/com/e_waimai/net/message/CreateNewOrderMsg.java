package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class CreateNewOrderMsg extends AMessage{
    public CreateNewOrderMsg() {
        super(IMessage.CREATE_NEW_ORDER);
    }
}
