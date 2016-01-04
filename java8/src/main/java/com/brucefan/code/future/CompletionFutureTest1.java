package com.brucefan.code.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bruce01.fan on 2016/1/4.
 */
public class CompletionFutureTest1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "--" + calNum().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();

        // get 方法是阻塞等待结果回来
        // getNow 避免等待，如果结果还没执行完，直接拿默认值
        //System.out.println(calNum().getNow(2L));

        //calNum().join();

        //System.out.println(createSupplyCF().get());

        //System.out.println(createRunCF().get());

        System.out.println(applyThen().get());

        printMe();
    }

    static CompletableFuture<Long> calNum() {
        final CompletableFuture<Long> future = new CompletableFuture<>();
        //future.complete(42L);
        // future.complete(44L); complete给结果赋值，但只允许第一个调用complete的生效，后续调用的都无效
        // 但可以通过obtrudeValue
        future.obtrudeValue(1L); // 强制修改complete的值
        return future;
    }


    /**
     * supplyAsync(Supplier<U> supplier)
     *
     * @return
     */
    static CompletableFuture<String> createSupplyCF() {
        // 使用默认的forkjoinPool
        CompletableFuture future = new CompletableFuture<>().supplyAsync(() -> {
            return findMe();
        });

        // 也可以自己定义线程池
        /*ExecutorService executors = Executors.newCachedThreadPool();
        future = new CompletableFuture<>().supplyAsync(()-> findMe(),executors);
        */
        return future;

    }

    /**
     * 运行Runnable，不带返回值的
     *
     * @return
     */
    static CompletableFuture<String> createRunCF() {
        CompletableFuture future = new CompletableFuture().runAsync(() -> {
        });
        return future;
    }

    /**
     * thenApply 等待结果返回之后继续往后处理
     *
     * @return
     */
    static CompletableFuture<Double> applyThen() {
        CompletableFuture<String> f1 = new CompletableFuture()
                .supplyAsync(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "10";
                });
        return f1.thenApply(Integer::parseInt).thenApply(r -> r * r * Math.PI);
    }


    static void printMe() {
        System.out.println("print me -------------");
    }

    static String findMe() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "George";
    }
}
