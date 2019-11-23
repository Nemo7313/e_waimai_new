package com.e_waimai.dbo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class BaseDBO {
    protected static Logger logger = LogManager.getLogger("APP");
    static Connection connection = null;
    static PreparedStatement psmt = null;
    static ResultSet rs;
    public boolean getConnection(){
        String driver = ConfigManager.getInstance().getKey("jdbc.driver_class");
        String url = ConfigManager.getInstance().getKey("jdbc.connection.url");
        String username = ConfigManager.getInstance().getKey("jdbc.connection.username");
        String password = ConfigManager.getInstance().getKey("jdbc.connection.password");
        try {
           connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            logger.error("数据库连接失败！");
            e.printStackTrace();
        }
        logger.debug("成功连接到远程服务器的数据库！");
        return true;
    }

    public void close(){
        if(null!= connection){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(null != psmt){
            try {
                psmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
