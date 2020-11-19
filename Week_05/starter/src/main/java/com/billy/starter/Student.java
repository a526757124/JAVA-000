package com.billy.starter;

import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String name;
    public  void init(){
        System.out.println("hello 张三");
    }
}
