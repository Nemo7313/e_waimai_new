package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

import java.util.Map;

public class AddPriceMsg extends AMessage{
    private Map<Long, Integer> bills;
    public AddPriceMsg(Map<Long, Integer> bills) {
        super(IMessage.ADD_PRICE);
        this.bills = bills;
    }

    public Map<Long, Integer> getBills() {
        return bills;
    }
}
