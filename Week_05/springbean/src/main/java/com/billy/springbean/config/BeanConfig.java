package com.billy.springbean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public Student loadStudent() {
        return new Student();
    }
}
