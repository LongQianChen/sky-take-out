package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {//注意：继承Orders类，
    // 因为Orders类中有订单信息，
    // 而OrderVO类中除了订单信息外，
    // 还要有订单菜品信息，所以继承Orders类，
    // 并添加订单菜品信息
    // 所以可以从orders中拷贝到orderVO

    //订单菜品信息
    private String orderDishes;

    //订单详情
    private List<OrderDetail> orderDetailList;

}
