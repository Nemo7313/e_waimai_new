package com.e_waimai.net.message;

public class FoodListMsg extends AMessage{
    private int RestaurantNumber;
    public FoodListMsg(int ResNumber) {
        super(Food_List);
        this.RestaurantNumber = ResNumber;
    }

    public int getRestaurantNumber() {
        return RestaurantNumber;
    }

}
