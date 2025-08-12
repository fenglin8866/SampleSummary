package com.xxh.lang.java.base.methodreferences;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Person {
    private String name;
    private int age;

    public Person(String name) {
        this.name = name;
    }

    public Person() {
    }

    public static void defaultName(String str) {
        System.out.println("name="+str);
    }

    public void defaultName2() {
        System.out.println();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean setName2(String name) {
        this.name = name;
        return true;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String consumer(Supplier<String> supplier){
        return supplier.get();
    }


}
