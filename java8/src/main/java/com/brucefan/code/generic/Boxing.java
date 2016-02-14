package com.brucefan.code.generic;

/**
 * Created by bruce01.fan on 2016/2/14.
 */
public class Boxing<T> implements Kongfu<T> {
    private T t;

    @Override
    public void study(T o) {
        this.t = t;
    }

    @Override
    public T fight() {
        return t;
    }

}
