package com.xxh.lang.java.base.methodreferences;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <a href=">https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">网站1</a>
 * <a href="http://ifeve.com/java-8-features-tutorial/">网站2</a>
 */

public class TestMethodReferences {

    public static void test0() {
        Person person=new Person("xxh");
        List<String> list=List.of("a", "b");
        //静态方法
        list.forEach(Person::defaultName);
        //实例方法
        list.forEach(person::setName2);
        list.forEach("b"::concat);

        list.forEach(Person::new);
        //list.forEach(person::getName);

        //特定类型任意对象方法引用
        list.forEach(String::toLowerCase);

        list.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                //"c".concat(s);
                //person.getName();
                s.toLowerCase();
            }
        });

        person.consumer("b"::toLowerCase);

    }

    public static void test1() {
        //构造方法
        Car car = Car.create(Car::new);
        Car car1=Car.create(new Supplier<Car>() {
            @Override
            public Car get() {
                return new Car();
            }
        });
        Car car2 = Car.create2("xtq",Car::new);
        List<Car> cars = Arrays.asList(car,car2);

        //引用静态方法
        cars.forEach(Car::collide);
        cars.forEach(new Consumer<Car>() {
            @Override
            public void accept(Car car) {
                Car.collide(car);
            }
        });

        //不是引用类的实例方法,是特定类型任意对象方法引用
        cars.forEach(Car::repair);
        cars.forEach(new Consumer<Car>() {
            @Override
            public void accept(Car car) {
                car.repair();
            }
        });

        //不是特定类型任意对象方法引用,是引用类的实例方法
        final Car police = Car.create(Car::new);
        cars.forEach(police::follow);

        cars.forEach(new Consumer<Car>() {
            @Override
            public void accept(Car car2) {
                police.follow(car2);
            }
        });
    }

}
