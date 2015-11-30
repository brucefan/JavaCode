package com.brucefan.code.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * synchronized vs atomic vs volatile
 * 线程安全意思就是当前程序在多线程情况下，能够按照规定好的约定执行，并保证结果的正确。
 * 线程安全包含两层意思：原子性 和 内存可见性
 * 原子性：保证一组操作它们是一个整体，状态必须保持一致
 * 内存可见性：一个线程修改了变量，其他线程也能看到之前线程修改的值
 * synchronized：能同时保证原子性与内存可见性
 * atomic: 能保证读-改-写这种行为的原子性,java针对基本数据类型提供了atomic的实现；它只能确保当前修改的一次行为是原子的，如果存在多次操作得话，线程就不是安全的了
 * volatile：能保证变量被修改后的内存可见性，但行为不能保证原子性，例如自增的行为
 * Created by bruce01.fan on 2015/11/30.
 */
public class SynchronzedVsAtomic {

    private static AtomicInteger incr = new AtomicInteger(0);
    private static volatile int incr2 = 0;


    public int atomicTest() {
        return incr.addAndGet(1);
    }

    public int volatileTest() {
        return incr2++;
    }

    public static void main(String[] args) {
        SynchronzedVsAtomic synchronzedVsAtomic = new SynchronzedVsAtomic();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                while (true) {
                    synchronized (synchronzedVsAtomic) {
                        //System.out.println();
                        int a = synchronzedVsAtomic.atomicTest();
                        int b = synchronzedVsAtomic.volatileTest();
                        if (a != b) {
                            System.out.println("wrong a:" + a + " b:" + b);
                        }
                    }
                }
            }).start();
        }
    }
}
