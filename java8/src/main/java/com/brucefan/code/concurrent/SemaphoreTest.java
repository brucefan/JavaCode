package com.brucefan.code.concurrent;


import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class SemaphoreTest {
    static AtomicInteger size = new AtomicInteger(100);

    public void call() {
        if (size.decrementAndGet() < 0) {
            System.err.println("超过上限咯");
        }
    }

    public static void main(String[] args) {

    }
}
