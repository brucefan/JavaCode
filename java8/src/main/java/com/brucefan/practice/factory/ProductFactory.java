package com.brucefan.practice.factory;

import com.brucefan.practice.bean.Product;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/1/16.
 */
public class ProductFactory implements Factory<Product> {

    static Logger logger = Logger.getLogger(ProductFactory.class.getName());
    static AtomicInteger size = new AtomicInteger(1);

    @Override
    public Product getById(Long id) {
        synchronized (this) {
            if (size.get() <= 0) {
                try {
                    logger.warn("已售完,挂起线程等待1秒");
                    //wait();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        logger.debug("Starting by create product");
        Product product = new Product(id, "点卡_" + System.currentTimeMillis(), "10.00", "游戏点卡", "大优惠，欢迎抢购");
        logger.debug("product finish [" + product.toString() + "]");
        size.decrementAndGet();
        return product;
    }
}
