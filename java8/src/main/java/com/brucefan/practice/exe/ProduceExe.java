package com.brucefan.practice.exe;

import com.brucefan.practice.bean.Product;
import com.brucefan.practice.service.ProduceServiceImpl;
import com.brucefan.practice.service.ProductService;
import org.apache.log4j.BasicConfigurator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/1/16.
 */
public class ProduceExe {

    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        ProductService productService = new ProduceServiceImpl();
        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                CompletableFuture<Product> productCompletableFuture = productService.getProductById(1L);
                try {
                    productCompletableFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
