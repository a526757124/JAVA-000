package com.billy.jdbcdemo.jdbc;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.UUID;


@Component
public class jdbcdemo implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String sourceURL = "jdbc:h2:mem:test";//H2DB mem mode
        String user = "sa";
        String key = "";
        try {
            Class.forName("org.h2.Driver");//HSQLDB Driver
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(sourceURL, user, key);
            Statement stmt = conn.createStatement();
            createTable(stmt);
            insert(stmt);
            showUser(stmt);
            update(stmt);
            showUser(stmt);
            delete(stmt);
            showUser(stmt);
            PreparedStatement(conn);
            showUser(stmt);
            //释放资源
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Statement stmt) throws SQLException {
        stmt.execute("DROP TABLE IF EXISTS USER_INFO");
        stmt.execute("CREATE TABLE USER_INFO(id VARCHAR(36) PRIMARY KEY,name VARCHAR(100),sex VARCHAR(4))");
        System.out.println("UserInfo表创建成功");
    }

    private void insert(Statement stmt) throws SQLException {
        stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID() + "','张三','男')");
        System.out.println("插入张三用户");
        stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID() + "','李四','男')");
        System.out.println("插入李四用户");
        stmt.executeUpdate("INSERT INTO USER_INFO VALUES('" + UUID.randomUUID() + "','王五','女')");
        System.out.println("插入王五用户");
    }

    private void update(Statement stmt) throws SQLException {
        stmt.executeUpdate("UPDATE USER_INFO SET name='赵六' WHERE name='李四'");
        System.out.println("修改李四名称为赵六");
    }

    private void delete(Statement stmt) throws SQLException {
        stmt.executeUpdate("DELETE FROM USER_INFO WHERE name='张三'");
        System.out.println("删除张三");
    }

    private void PreparedStatement(Connection conn) {
        try {
            PreparedStatement prepareStatement1 = conn.prepareStatement("INSERT INTO USER_INFO VALUES(?,?,?)");
            prepareStatement1.setObject(1, UUID.randomUUID());
            prepareStatement1.setString(2, "XX");
            prepareStatement1.setString(3, "未知");
            prepareStatement1.executeLargeUpdate();
            PreparedStatement prepareStatement2 = conn.prepareStatement("UPDATE USER_INFO SET name=? WHERE name=?");
            prepareStatement2.setString(1, "王五五");
            prepareStatement2.setString(2, "王五");
            prepareStatement2.executeLargeUpdate();
            try {
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                prepareStatement1.close();
                prepareStatement2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showUser(Statement stmt) throws SQLException {
        //查询
        ResultSet rs = stmt.executeQuery("SELECT * FROM USER_INFO");
        //遍历结果集
        while (rs.next()) {
            System.out.println(rs.getString("id") + "," + rs.getString("name") + "," + rs.getString("sex"));
        }
    }
}
