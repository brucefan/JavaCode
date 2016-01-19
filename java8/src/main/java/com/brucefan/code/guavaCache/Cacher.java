package com.brucefan.code.guavaCache;

import com.brucefan.code.concurrent.SynchronzedVsAtomic;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.concurrent.*;

/**
 */
public class Cacher {
    static LoadingCache<String, Person> builder;
    static ExecutorService executors = Executors.newFixedThreadPool(1);

    static {
        builder = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .refreshAfterWrite(1, TimeUnit.MILLISECONDS)
                        //.expireAfterWrite(1, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Person>() {
                    @Override
                    public Person load(String s) throws Exception {
                        return null;
                    }

                    @Override
                    public ListenableFuture<Person> reload(String key, Person oldValue) throws Exception {
                        // 异步
                        ListenableFutureTask<Person> task = ListenableFutureTask.create(new Callable<Person>() {
                            @Override
                            public Person call() throws Exception {
                                System.out.println("reload person");
                                return null;
                            }
                        });
                        executors.execute(task);
                        return task;
                    }
                });
    }


    public static void main(String[] args) {
        /*System.out.println(builder.get("bruce"));
        Thread.sleep(2000);
        System.out.println(builder.get("bruce"));*/

        try {
            String a = null;
            a.toCharArray();
        } catch (RuntimeException npe) {
            System.out.println(2);
        } catch (Exception e) {
            System.out.println(1);
        }
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
