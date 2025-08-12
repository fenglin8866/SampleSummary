package com.xxh.lang.java.base.methodreferences;

import java.util.function.Function;
import java.util.function.Supplier;

class Car{
    private String name="xxh";

    public Car(String name) {
        this.name = name;
    }

    public Car() {
    }

    public static Car create(final Supplier<Car> supplier ) {
        return supplier.get();
    }

    public static Car create2(String name, final Function<String, Car> supplier) {
        return supplier.apply(name);
    }

    public static void collide( final Car car ) {
        System.out.println( "Collided " + car.toString() );
    }

    public void follow( final Car another ) {
        System.out.println( "Following the " + another.toString() );
    }

    public String repair() {
        System.out.println( "Repaired " + this);
        return "";
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                '}';
    }
}
