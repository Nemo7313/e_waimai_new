package com.e_waimai.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * 网络客户端父类，完成基础网络消息收发工作，供各客户端继承实现业务逻辑
 */
public abstract class ANetClient implements INetClient {
    protected static Logger logger = LogManager.getLogger("APP");
    private Socket socket; //客户端Socket
    private boolean loop = true;//线程循环控制
    private boolean inServer = false;//是否服务器端线程

    /**
     * 客户端创建用
     * @param host
     * @param port
     */
    protected ANetClient(String host, int port){
        logger.debug("准备创建客户端，连接地址-"+host+":"+port);
        try {
            socket = new Socket(host, port);
            logger.debug("客户端连接服务器成功-"+host+":"+port);
        } catch (IOException e) {
            logger.fatal(e);
            e.printStackTrace();
        }
    }

    /**
     * 服务端创建用
     * @param socket 获取到的客户端socket
     */
    protected ANetClient(Socket socket) {
        try {
            this.socket = socket;
            inServer = true;
            logger.debug("服务端创建了与客户端相连接的线程");
        }catch (Exception e){
            logger.fatal("e");
            e.printStackTrace();
        }
    }

    @Override
    public boolean send(IMessage msg) {
        try {
            OutputStream os = this.socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("消息发送失败！");
            return false;
        }
        logger.debug("消息发送完毕！");
        return true;
    }

    @Override
    public abstract void onMessage(IMessage event);

    @Override
    public void run() {
        do{
            try {
                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object oMsg = ois.readObject();
                logger.debug("获取输入对象");
                if(null!=oMsg){
                    IMessage msg = (IMessage)oMsg;
                    onMessage(msg);
                    logger.debug("客户端消息处理完毕");
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e);
                if(this.inServer){
                    //服务器端线程如果接收异常，踢掉线程，停止线程
                    this.stop();
                }
                logger.debug("消息解码失败！");
            }
        }while(loop);
        logger.info("客户端线程结束！");
    }

    public void stop(){
        this.loop = false;
    }

    public void close(){
        loop = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
            logger.debug("Socket关闭异常！");
        }
    }
}
