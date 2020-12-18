package com.billy.starter.annotaion;



@MyAnno3
@MyAnno(value = 12, per = Person.P1, anno2 = @MyAnno2, strs = "aaa")
public class Worker {
    @MyAnno3
    public  String name="aa";
    @MyAnno3
    public  void show(){

    }
}
