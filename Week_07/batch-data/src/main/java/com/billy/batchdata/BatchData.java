package com.billy.batchdata;

import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BatchData {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        demo2();
    }
    public static  void demo1() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://192.168.200.183:3307/dockertest?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        Connection connection= DriverManager.getConnection(url,"root","123456");
        try {
            long startTime=System.currentTimeMillis();
            String insertSQL = "insert into user ( `name`, mobile,status) values(?,?,?)";
            PreparedStatement pst = connection.prepareStatement(insertSQL);
            for (int i = 0; i < 1000000; i++) {
                pst.setString(1, "name"+i);
                pst.setString(2, "test" + i);
                pst.setInt(3, 0);
                pst.addBatch();
            }
            pst.executeBatch();
            pst.close();
            connection.close();
            long endTime=System.currentTimeMillis();
            System.out.println("执行时间: {"+(endTime-startTime)+"} ms" );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  void demo2() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://192.168.200.183:3307/dockertest?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        Connection connection= DriverManager.getConnection(url,"root","123456");
        try {
            connection.setAutoCommit(false);
            long startTime=System.currentTimeMillis();
            StringBuffer sqlBuffer = new StringBuffer();
            String insertSQL = "insert into user ( `name`, mobile,status) values";
            PreparedStatement pst = connection.prepareStatement(insertSQL);
            for (int i = 0; i < 10; i++) {
                for (int j=0;j<100000;j++){
                    sqlBuffer.append("('name','test',0),");
                }
                // 构建完整sql
                String sql = insertSQL + sqlBuffer.substring(0, sqlBuffer.length() - 1);
                // 添加执行sql
                pst.addBatch(sql);
                // 执行操作
                pst.executeBatch();
                sqlBuffer=sqlBuffer.delete(0,sqlBuffer.length());
            }
            // 提交事务
            connection.commit();
            pst.close();
            connection.close();
            long endTime=System.currentTimeMillis();
            System.out.println("执行时间: {"+(endTime-startTime)+"} ms" );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
