package com.xxh.lang.java.base.concurrent;

import com.xxh.lang.java.base.Utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestCompletedFuture {
    public static void test() {
        //parallel();
       // serial();
        callback();
    }

    public static void parallel() {
        long startTime = System.currentTimeMillis();
        CompletableFuture future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Utils.intervalTime("f1", startTime);
                return "xxh";
            }
        }).thenCombine(CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Utils.intervalTime("f2", startTime);
                return "xtq";
            }
        }), (s1, s2) -> {
            String result = s1 + s2;
            Utils.intervalTime(result, startTime);
            return result;
        });
        future.join();
    }

    public static void serial(){
        long startTime = System.currentTimeMillis();
        CompletableFuture future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Utils.intervalTime("serial f1", startTime);
                return "xxh";
            }
        }).thenApply(new Function<String, String>() {
            @Override
            public String apply(String s) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Utils.intervalTime("serial f2", startTime);
                return s+" xtq";
            }
        }).whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                Utils.intervalTime("serial result="+s, startTime);
            }
        });
        future.join();
    }

    public static void callback(){
        long startTime = System.currentTimeMillis();
        CompletableFuture future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Utils.intervalTime("callback f1", startTime);
                return "xxh";
            }
        });

        CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    //并行获取接口
                    String result= (String) future.get();
                    Utils.intervalTime("callback xtq "+result, startTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

    }


}
