package com.billy.jdbcdemo.hikari;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class hikaridemo implements ApplicationRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(jdbcTemplate);
        createTable();
        insert();
        showUser();
        update();
        showUser();
        delete();
        showUser();
    }

    private void createTable(){
        jdbcTemplate.execute("DROP TABLE IF EXISTS USER_INFO");
        jdbcTemplate.execute("CREATE TABLE USER_INFO(id VARCHAR(36) PRIMARY KEY,name VARCHAR(100),sex VARCHAR(4))");
        System.out.println("UserInfo表创建成功");
    }

    private void insert()  {
        jdbcTemplate.update("INSERT INTO USER_INFO VALUES(?,?,?)", UUID.randomUUID(), "张三", "男");
        System.out.println("插入张三用户");
        jdbcTemplate.update("INSERT INTO USER_INFO VALUES(?,?,?)", UUID.randomUUID(), "李四", "男");
        System.out.println("插入李四用户");
        jdbcTemplate.update("INSERT INTO USER_INFO VALUES(?,?,?)", UUID.randomUUID(), "王五", "女");
        System.out.println("插入王五用户");
    }

    private void update() {
        jdbcTemplate.update("UPDATE USER_INFO SET name=? WHERE name=?", "赵六", "李四");
        System.out.println("修改李四名称为赵六");
    }

    private void delete() {
        jdbcTemplate.update("DELETE FROM USER_INFO WHERE name=?", "张三");
        System.out.println("删除张三");
    }

    private void showUser() {
        //查询
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("SELECT * FROM USER_INFO");
        for (Map<String, Object> map : mapList) {
            System.out.println(map.get("id") + "," + map.get("name") + "," + map.get("sex"));
        }
    }
}
