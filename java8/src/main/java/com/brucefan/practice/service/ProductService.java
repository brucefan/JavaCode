package com.brucefan.practice.service;

import com.brucefan.practice.bean.Product;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Administrator on 2016/1/16.
 */
public interface ProductService {

    public CompletableFuture<Product> getProductById(Long id);
}
