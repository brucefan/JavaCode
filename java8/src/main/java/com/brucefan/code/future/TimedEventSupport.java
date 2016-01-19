package com.brucefan.code.future;

import com.brucefan.code.concurrent.IntegerAtomicTest;
import org.omg.CORBA.UNSUPPORTED_POLICY;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

/**
 */
public class TimedEventSupport {
    static Timer timer = new Timer();

    static <T> CompletableFuture<T> delayedSuccess(int delay, T value) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                completableFuture.complete(value);
            }
        };
        timer.schedule(timerTask, delay * 1000);
        return completableFuture;
    }

    static <T> CompletableFuture<T> delayedFail(int delay, Throwable t) {
        CompletableFuture<T> future = new CompletableFuture<>();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                future.completeExceptionally(t);
            }
        };
        timer.schedule(timerTask, delay * 1000);
        return future;
    }

    static CompletableFuture<Integer> Task1(int input) {
        CompletableFuture<Integer> future = TimedEventSupport.delayedSuccess(1, input + 1);
        return future;
    }

    static CompletableFuture<Integer> Task2(int input) {
        return TimedEventSupport.delayedSuccess(2, input + 2);
    }

    static CompletableFuture<Integer> Task3(int input) {
        return TimedEventSupport.delayedSuccess(3, input + 3);
    }

    static CompletableFuture<Integer> Task4(int input) {
        return TimedEventSupport.delayedSuccess(1, input + 4);
    }

    static CompletableFuture<Integer> Task5(int input) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        try {
            String a = null;
            a.toString();
            future = TimedEventSupport.delayedSuccess(1, input + 1);
        } catch (Exception e) {
            // 异常如果通过throw的方法抛出的时候，那么该方法不能在thenCompose，thenCombin中嵌套使用
            future.completeExceptionally(new IllegalClassFormatException());
            // throw new IllegalClassFormatException();
        }
        return future;
    }

    static Integer blockingRun() {
        Integer val_1 = Task1(100).join();
        CompletableFuture<Integer> future2 = Task2(val_1);
        CompletableFuture<Integer> future3 = Task3(val_1);

        return Task4(future2.join() + future3.join()).join();
    }

    static Integer unblockingRun() {
        return Task1(100).thenComposeAsync(val_1 -> (Task2(val_1)).thenCombineAsync(Task3(val_1), (val_2, val_3) -> (val_2 + val_3))).thenComposeAsync(val_4 -> Task4(val_4)).join();
    }

    static Integer errRun() throws ExecutionException, InterruptedException {
        Task1(100).thenCompose(val_1 -> Task5(4)).get();
        return 0;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*System.out.println("Starting on blocking future tasks ");
        long start = System.currentTimeMillis();
        Integer a = blockingRun();
        long end = System.currentTimeMillis();
        System.out.println("Ending on blocking future tasks. total times:" + (end - start) + " , value:" + a);
*/
        /*System.out.println("Starting on unblocking future tasks ");
        long start2 = System.currentTimeMillis();
        Integer b = unblockingRun();
        long end2 = System.currentTimeMillis();
        System.out.println("Ending on unblocking future tasks. total times:" + (end2 - start2) + " , value:" + b);*/

        errRun();

    }
}
