package com.billy.springbean.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigDemo {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
        //根据ID从容器容获取bean
        Student stu = (Student) context.getBean("loadStudent");
        System.out.println(stu);
    }
}
