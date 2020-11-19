package com.billy.springbean.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = ScanConfig.class)
public  class  ScanDemo {
    @Autowired
    private static Student student;
    public static void main(String[] args) {
    }
}
