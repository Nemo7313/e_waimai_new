package com.e_waimai.dbo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class CustomerDBO extends BaseDBO {
    protected static Logger logger = LogManager.getLogger("APP");
    private int update = 0;

    public String getCustomerInfo(String tel){
        String info = null;
        if(getConnection()){
            String sql = "select * from customer where tel = ?";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setString(1,tel);
                rs = psmt.executeQuery();
                while(rs.next()){
                    String name = rs.getString("name");
                    String addr = rs.getString("addr");
                    info = "您的姓名为："+name+", 你的配送地址为："+addr;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return info;
    }
    public boolean register(String tel, String name, String addr){
        if(getConnection()){
            String sql = "INSERT INTO customer(tel, name, addr) VALUES(?,?,?)";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setString(1,tel);
                psmt.setString(2,name);
                psmt.setString(3,addr);
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

    public long getIdByPhone(String phone){
        long customerID = -1;
        if(getConnection()){
            String sql = "select id from customer where tel = ?";
            try {
                psmt = connection.prepareStatement(sql);
                psmt.setString(1,phone);
                rs = psmt.executeQuery();
                while(rs.next()){
                    customerID = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customerID;
    }

}
