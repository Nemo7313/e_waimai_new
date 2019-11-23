package com.e_waimai;

import com.e_waimai.net.NetServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
   public static Logger logger = LogManager.getLogger("APP");

    public static void main(String[] args) {
        logger.debug("外卖服务器程序启动");

        // 启动网络服务器
        NetServer server = new NetServer(9999);
        if(server.start()){
            logger.debug("启动成功！端口：9999  等待连接");
            new Thread(server).start();
        }else{
            logger.debug("启动失败！");
        }

    }
}
