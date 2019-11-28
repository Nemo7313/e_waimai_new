package com.e_waimai.dbo;

import java.sql.SQLException;
import java.util.Map;

public class PlatformDBO extends BaseDBO {
    private int update = 0;

    public boolean createNewOrderAndGetId(long customerId, int restaurantId, double deliCost, double totalCost,
                                       Map<Long, Integer> foodRecord){
        long orderId = 0;
        if(getConnection()){
            String sql = "INSERT INTO `order`(customer_id, restaurant_id, deli_cost, total_cost) VALUES(?,?,?,?)";
            try {
                //开启事务
                connection.setAutoCommit(false);
                //创建订单
                psmt = connection.prepareStatement(sql);
                psmt.setLong(1,customerId);
                psmt.setInt(2,restaurantId);
                psmt.setDouble(3, deliCost);
                psmt.setDouble(4, totalCost);
                update  = psmt.executeUpdate();
                if(update != 1){
                    connection.rollback();
                    logger.debug("数据有误，已回滚！");
                }
                //获取新订单的Id
                long newOrderId = getOrderId();
                logger.debug("新订单的Id为"+newOrderId);
                if(newOrderId == -1){
                    connection.rollback();
                    logger.debug("数据有误，已回滚！");
                }
                //插入菜单信息
                int recordNum = 0;
                for(Long foodId:foodRecord.keySet()){
                    recordNum += addRecord(newOrderId, foodId, foodRecord.get(foodId));
                }
                logger.debug("在record表中新插入了"+recordNum+"行数据");
                logger.debug("菜单中的菜品种类为"+foodRecord.keySet().size());
                //判断插入的所有数据是否和账单Map里Key的个数一样；
                if(recordNum != foodRecord.keySet().size()){
                    connection.rollback();
                    logger.debug("数据有误，已回滚！");
                }

                connection.commit();
                connection.setAutoCommit(true);
                logger.debug("已完成对数据库order和record两张表的数据插入操作");
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    static long getOrderId(){
        long orderId = -1;
        String sql = "SELECT LAST_INSERT_ID()";
        try {
            psmt = connection.prepareStatement(sql);
            rs = psmt.executeQuery();
            while(rs.next()){
                orderId = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    static int addRecord(long oid, long fid, int num){
        int updateRow = 0;
        String sql = "insert into record(order_id, food_id, count) values (?,?,?)";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setLong(1,oid);
            psmt.setLong(2,fid);
            psmt.setInt(3,num);
            updateRow = psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateRow;
    }
}
