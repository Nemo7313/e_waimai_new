package com.e_waimai.dbo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

public class RestaurantDBO extends BaseDBO {
    protected static Logger logger = LogManager.getLogger("APP");
    private int update = 0;

    public boolean register(String tel, String name, String addr, String notes){
        if(getConnection()){
            String sql = "INSERT INTO restaurant(tel, name, addr, notes) VALUES(?,?,?,?)";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setString(1,tel);
                psmt.setString(2,name);
                psmt.setString(3,addr);
                psmt.setString(4,notes);
                update  = psmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(update == 1){
            return true;
        }else{
            return false;
        }
    }

    public int getIdByPhone(String phone){
        int RestuarantID = -1;
        if(getConnection()){
            String sql = "select id from restaurant where tel = ?";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setString(1,phone);
                rs = psmt.executeQuery();
                while(rs.next()){
                    RestuarantID = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return RestuarantID;
    }

    public String getRestaurantInfo(String tel){
        String info = null;
        if(getConnection()){
            String sql = "select * from restaurant where tel = ?";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setString(1,tel);
                rs = psmt.executeQuery();
                while(rs.next()){
                    String name = rs.getString("name");
                    String addr = rs.getString("addr");
                    String notes = rs.getString("notes");
                    info = "您的店名为："+name+", 你的地址为："+addr+",您的商家简介为："+notes;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    public boolean addfood(int rid, String fname, double fprice, String fIntro){
        int row = 0;
        if(getConnection()){
            String sql = "INSERT into food(name, price, restaurant_id, intro) values(?, ? ,?, ?)";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setString(1,fname);
                psmt.setDouble(2,fprice);
                psmt.setInt(3,rid);
                psmt.setString(4, fIntro);
                row = psmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(row ==1) {
            return true;
        }else{
            return false;
        }
    }

    public String getRestaurantList(){
        StringBuilder stringBuilder = new StringBuilder();
        if(getConnection()){
            String sql = "select id,name,notes from restaurant";
            try {
                psmt = connection.prepareStatement(sql);
                rs = psmt.executeQuery();
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String notes = rs.getString("notes");
                    logger.debug("id是--"+id+"姓名是---"+name+"简介是----"+notes);
                    stringBuilder.append(id);
                    stringBuilder.append("\t");
                    stringBuilder.append(name);
                    stringBuilder.append("\t");
                    stringBuilder.append(notes);
                    stringBuilder.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.debug("已完成客户请求的餐馆列表");
        return stringBuilder.toString();
    }

    public String getFoodList(int ResId){
        StringBuilder stringBuilder2 = new StringBuilder();
        if(getConnection()){
            String sql = "select id,name,price,intro from food where restaurant_id = ?";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setInt(1,ResId);
                rs = psmt.executeQuery();
                while(rs.next()){
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    BigDecimal price = rs.getBigDecimal("price");
                    String notes = rs.getString("intro");
                    stringBuilder2.append(id);
                    stringBuilder2.append("\t");
                    stringBuilder2.append(name);
                    stringBuilder2.append("\t");
                    stringBuilder2.append(price);
                    stringBuilder2.append("\t");
                    stringBuilder2.append(notes);
                    stringBuilder2.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.debug("已完成客户请求的菜单列表");
        return stringBuilder2.toString();
    }

    public double getFoodPrice(long foodId){
        double price = 0.0;
        if(getConnection()){
            String sql = "select price from food where id = ?";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setLong(1,foodId);
                rs = psmt.executeQuery();
                while(rs.next()){
                    price = rs.getDouble("price");
                    logger.debug("此菜品的价格为"+price +"元");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return price;
    }
}
