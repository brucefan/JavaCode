package com.brucefan.practice.cache;

import com.brucefan.practice.bean.Product;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/1/16.
 */
public class CacheLoaderPool {
    public static LoadingCache<Long, Product> productLoadingBuilder;
    static ExecutorService executor = Executors.newFixedThreadPool(2);

    static {
        buildProductLoadingCache();
    }

    private static void buildProductLoadingCache() {
        productLoadingBuilder = CacheBuilder.newBuilder()
                .maximumSize(1000)
                // expireAfterWrite 被写后n个时间间隔后会自动回收过期，待下个请求过来后再去获取新的数据
                // 其他请求阻塞等待当前线程返回值并设置回本地缓存中
                .expireAfterWrite(200, TimeUnit.MICROSECONDS)
                // 刷新机制，当到达可刷新的时间时，并且有新请求要获取缓存内容时，
                .refreshAfterWrite(100, TimeUnit.MICROSECONDS)
                .recordStats()
                .build(new ProductLoader(executor));
    }

}
