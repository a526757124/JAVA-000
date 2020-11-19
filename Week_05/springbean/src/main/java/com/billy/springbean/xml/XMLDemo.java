package com.billy.springbean.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLDemo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        //XML的方式如果没有明确给定ID，默认bean的ID会根据类的全限定名来命名，以#加计数序号的方式命名。
        Student student = (Student)context.getBean("student");
        System.out.println(student);
    }
}
