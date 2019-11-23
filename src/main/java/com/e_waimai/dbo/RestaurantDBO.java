package com.e_waimai.dbo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

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
}
