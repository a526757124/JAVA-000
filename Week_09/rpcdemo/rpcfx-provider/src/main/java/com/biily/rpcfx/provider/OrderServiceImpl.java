package com.biily.rpcfx.provider;

import com.billy.rpcfx.api.Order;
import com.billy.rpcfx.api.OrderService;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return  new Order(id,"Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
