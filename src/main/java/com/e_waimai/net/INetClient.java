package com.e_waimai.net;

public interface INetClient extends Runnable, IMsgHandler {
    /**
     *向该客户端发送消息
     * @param msg 待发送的消息
     * @return 消息发送与否
     */
        boolean send(IMessage msg);
}
