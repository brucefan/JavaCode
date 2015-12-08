package com.brucefan.code.guavaCache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by bruce01.fan on 2015/12/8.
 */
public class Cacher {
    static LoadingCache<String, Person> builder;

    static {
        builder = CacheBuilder.newBuilder()
                .maximumSize(1000)
                        //.refreshAfterWrite(1, TimeUnit.SECONDS)
                        //.expireAfterWrite(1,TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Person>() {
                    @Override
                    public Person load(String s) throws Exception {
                        return new Person(s + "_" + System.currentTimeMillis());
                    }
                });
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        builder.get("bruce");
        //builder.get("bruce");
        //Thread.sleep(1000);
        //builder.get("bruce");
    }

    static class Person {
        private String name;

        public Person(String name) {
            System.out.println("init person name:->" + name);
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Person{");
            sb.append("name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
