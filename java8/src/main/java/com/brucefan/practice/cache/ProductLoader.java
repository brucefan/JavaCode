package com.brucefan.practice.cache;

import com.brucefan.practice.bean.Product;
import com.brucefan.practice.factory.ProductFactory;
import com.brucefan.practice.service.ProduceServiceImpl;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;

/**
 * Created by Administrator on 2016/1/16.
 */
public class ProductLoader extends CacheLoader<Long, Product> {
    static Logger logger = Logger.getLogger(ProductLoader.class);
    private ExecutorService exector;

    public ProductLoader(ExecutorService exector) {
        this.exector = exector;
    }

    ProductFactory factory = new ProductFactory();

    @Override
    public Product load(Long key) throws Exception {
        return factory.getById(key);
    }

    @Override
    public ListenableFuture<Product> reload(Long key, Product oldValue) throws Exception {
        logger.debug("reload >>>>>>>>>> ");
        ListenableFutureTask task = ListenableFutureTask.create(() -> {
            logger.info(">>>>>>>>reload product");
            return factory.getById(key);
        });
        exector.execute(task);
        return task;
    }
}
