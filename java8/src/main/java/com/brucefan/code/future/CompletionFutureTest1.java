package com.brucefan.code.future;

import com.brucefan.code.concurrent.SynchronzedVsAtomic;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
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

        // System.out.println(applyThen().get());

        // System.out.println(exceptionFuture().get());

        // System.out.println(handleFuture().get());

        combinFutures();
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
        // Apply接受等待结果返回后对结果进行相应的操作 相当于map
        return f1.thenApply(Integer::parseInt).thenApply(r -> r * r * Math.PI);
    }

    static CompletableFuture<String> exceptionFuture() {
        // 如果future结果有异常，则返回异常中的提示信息
        CompletableFuture<String> future = new CompletableFuture().supplyAsync(() -> {
            int[] a = null;
            a[1] = 0;
            return "Test";
        }).exceptionally(ex -> "my exception: " + ex.getMessage());

        return future;
    }

    static CompletableFuture<Integer> handleFuture() {
        CompletableFuture<Integer> future = new CompletableFuture<>().supplyAsync(() -> {
            int[] a = null;
            a[1] = 0;
            return 9;
        });

        // handle可以针对成功后异常情况做处理（二元处理）
        CompletableFuture<Integer> safe = future.handle((ok, ex) -> {
            if (ok != null) {
                return ok;
            } else {
                System.out.println("ex ->" + ex.getMessage());
                return -1;
            }
        });
        return safe;
    }

    CompletableFuture<Double> composeFutures() {
        CompletableFuture<Integer> f1 = new CompletableFuture().supplyAsync(() -> {
            return 3;
        });

        // 涉及嵌套Future的情况，thenApply只能将返回的类型作为嵌套的方式返回，类似于map方式
        CompletableFuture<CompletableFuture<Double>> ddf = f1.thenApply(result -> {
            CompletableFuture<Double> doubleFuture = new CompletableFuture().supplyAsync(() -> {
                return result * result * Math.PI;
            });
            return doubleFuture;
        });

        // thenCompose类似flatMap，可以将多层嵌套的结构进行扁平化处理
        CompletableFuture<Double> df = f1.thenComposeAsync(result -> {
            CompletableFuture<Double> doubleFuture = new CompletableFuture().supplyAsync(() -> {
                return result * result * Math.PI;
            });
            return doubleFuture;
        });

        return null;
    }


    static void combinFutures() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> bigFuture = new CompletableFuture<>().supplyAsync(() -> {
            long big = 0L;
            for (int i = 0; i < 200000; i++) {
                big += i * (i + 1);
            }
            System.out.println("world big done");
            return 1L;
        });

        CompletableFuture<Long> widthFuture = new CompletableFuture<>().supplyAsync(() -> {
            long width = 0L;
            for (int i = 0; i < 200000; i++) {
                width += i * (i + 3);
            }
            System.out.println("world width done");
            return 2L;
        });

        // 等待两个future完成再消费掉 (Accept 消费)
        CompletableFuture voidFuture = bigFuture.thenAcceptBoth(widthFuture, (a, b) -> {
            Long c = a + b;
            System.out.println("done -> " + c);
        });

        // 返回两个future中最快的一个
        CompletableFuture<Long> either = bigFuture.applyToEitherAsync(widthFuture, s -> {
            System.out.println(s);
            return s;
        });

        System.out.println("************** " + either.get());

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

    static class World {
        long big;
        long width;

        public World(long big, long width) {
            this.big = big;
            this.width = width;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("World{");
            sb.append("big=").append(big);
            sb.append(", width=").append(width);
            sb.append('}');
            return sb.toString();
        }
    }
}
