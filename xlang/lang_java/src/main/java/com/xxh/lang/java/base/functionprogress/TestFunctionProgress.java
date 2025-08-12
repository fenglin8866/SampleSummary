package com.xxh.lang.java.base.functionprogress;

public class TestFunctionProgress {
    public static void test1() {
        Handler handler = new Handler();
        Operator operator = new Operator();
        handler.handle(4, 2, operator::add);
        handler.handle(4, 2, operator::sub);
        handler.handle(4, 2, operator::multip);
        handler.handle(4, 2, operator::division);
        handler.handle2(3,4,operator::add1);
    }
}
