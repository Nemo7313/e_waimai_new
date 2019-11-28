package com.e_waimai.business;

import com.e_waimai.dbo.CustomerDBO;
import com.e_waimai.dbo.PlatformDBO;
import com.e_waimai.dbo.RestaurantDBO;

public class Platform {
    protected RestaurantDBO restaurantDBO = new RestaurantDBO();
    protected CustomerDBO customerDBO = new CustomerDBO();
    protected PlatformDBO platformDBO = new PlatformDBO();
}
