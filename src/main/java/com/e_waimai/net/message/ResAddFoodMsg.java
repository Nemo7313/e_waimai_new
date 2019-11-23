package com.e_waimai.net.message;

import com.e_waimai.net.IMessage;

public class ResAddFoodMsg extends AMessage {
    private String name;
    private double price;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getIntro() {
        return intro;
    }

    private String intro;
    public ResAddFoodMsg(String name, double price, String intro) {
        super(IMessage.RS_ADD_FOOD);
        this.name = name;
        this.price = price;
        this.intro = intro;
    }
}
