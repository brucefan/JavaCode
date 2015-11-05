package com.brucefan.code.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Created by bruce01.fan on 2015/11/4.
 */
public class IntegerAtomicTest {

    private static int id = 0;

    private static AtomicInteger atomicId = new AtomicInteger(); // 基于CPU原语CAS来实现原子性操作

    private static CountDownLatch latch = null;

    public synchronized static int getNextId() {
        return id++;
    }

    public static int getNextIdAtomic() {
        return atomicId.decrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {

        latch = new CountDownLatch(50);
        long beginTime = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    getNextId();
                }
                latch.countDown(); // 每次释放一个，当到达0时，释放所有等待的线程
                //System.out.println(latch.toString());
            }).start();
        }
        //当latch中的数量全部被消费完后（size==0 or 被中断）才终止等待
        latch.await();
        long endTime = System.nanoTime();
        System.out.println("synchronized 实现int 递增耗时:" + (endTime - beginTime));

        latch = new CountDownLatch(50);
        beginTime = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    getNextIdAtomic();
                }
                latch.countDown(); // 每次释放一个，当到达0时，释放所有等待的线程
                //System.out.println(latch.toString());
            }).start();
        }
        //当latch中的数量全部被消费完后（size==0 or 被中断）才终止等待
        latch.await();
        endTime = System.nanoTime();
        System.out.println("atomicInteger 实现int 递增耗时:" + (endTime - beginTime));

    }


}
