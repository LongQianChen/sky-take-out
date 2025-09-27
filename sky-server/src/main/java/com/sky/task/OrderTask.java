package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 定时处理处于待支付状态的订单
     */
    @Scheduled(cron = "0 * * * * ? ")//每分钟执行一次
    public void processTimeoutOrder(){
        log.info("定时处理超时订单: {}", LocalDateTime.now());

        // select * from orders where status = ? and order_time < (当前时间-15分钟)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(15));

        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                //更新订单状态
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("支付超时");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 定时处理处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")//每天凌晨1点执行一次
    public void processDeliveryOrder(){
        log.info("定时处理一直处于派送中的订单: {}", LocalDateTime.now());

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusMinutes(60));

        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                //更新订单状态
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
}
