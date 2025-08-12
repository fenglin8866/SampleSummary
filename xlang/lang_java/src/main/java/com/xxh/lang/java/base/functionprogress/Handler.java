package com.xxh.lang.java.base.functionprogress;

import java.util.function.BiFunction;

public class Handler {

    public String handle(int a, int b, BiFunction<Integer,Integer,String> biFunction){
        String result="result: "+biFunction.apply(a,b);
        System.out.println(result);
       return result;
    }

    public <T> T handle2(int a, int b, BiFunction<Integer,Integer,T> biFunction){
        return biFunction.apply(a,b);
    }
}
