package com.brucefan.practice.service;

import com.brucefan.practice.bean.Product;
import com.brucefan.practice.cache.CacheLoaderPool;
import com.brucefan.practice.factory.Factory;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2016/1/16.
 */
public class ProduceServiceImpl implements ProductService {
    Logger logger = Logger.getLogger(ProduceServiceImpl.class.getName());

    @Override
    public CompletableFuture<Product> getProductById(Long id) {
        CompletableFuture<Product> productFuture = new CompletableFuture<>();
        try {
            logger.debug("finding Prouct By id:[" + id + "].");
            Product product = CacheLoaderPool.productLoadingBuilder.get(id);
            logger.debug("return product name-> " + product.getName());
            productFuture.complete(product);
        } catch (ExecutionException e) {
            productFuture.completeExceptionally(e);
        }
        return productFuture;
    }
}
