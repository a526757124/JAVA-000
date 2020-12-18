package com.billy.rpcfx.consumer;

import com.billy.rpcfx.api.Order;
import com.billy.rpcfx.api.OrderService;
import com.billy.rpcfx.api.User;
import com.billy.rpcfx.api.UserService;
import com.billy.rpcfx.client.Rpcfx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcfxConsumerApplication {

    public static void main(String[] args) {

        UserService userService = Rpcfx.create(UserService.class, "http://localhost:8080/");
        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());

        OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
        Order order = orderService.findOrderById(1992129);
        System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));


        //SpringApplication.run(RpcfxConsumerApplication.class, args);
    }

}
