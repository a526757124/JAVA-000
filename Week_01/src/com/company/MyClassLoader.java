package com.company;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Class<?> hello = new MyClassLoader().findClass("Hello");
            if(hello == null) return;
            Method method = hello.getMethod("hello");
            Object obj = hello.newInstance();
            method.invoke(obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes=this.getBytesByFilePath(this.getClass().getResource("Hello.xlass").getPath());
        if(bytes == null) return null;
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] getBytesByFilePath(String filePath) {
        try {
            File file = new File(filePath);
            byte[] bytes= Files.readAllBytes(file.toPath());
            for (int i=0;i<bytes.length;i++){
                bytes[i]=(byte)(0xFF-bytes[i]);
            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
