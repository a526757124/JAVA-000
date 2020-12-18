package com.billy.starter.annotaion;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Pro(className = "com.billy.starter.annotaion.Demo2",methodName = "show")
public class ReflectTest {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        Class<ReflectTest> reflectTestClass=ReflectTest.class;
        Pro an=reflectTestClass.getAnnotation(Pro.class);
        String className= an.className();
        String methodName=an.methodName();

        Class cls=Class.forName(className);
        Object obj=cls.newInstance();
        Object [] paras =  {"test"};
        Method method=cls.getMethod(methodName);

        method.invoke(obj);
    }
}
