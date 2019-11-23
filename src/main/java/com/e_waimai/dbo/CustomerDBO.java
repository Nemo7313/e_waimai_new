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
}
