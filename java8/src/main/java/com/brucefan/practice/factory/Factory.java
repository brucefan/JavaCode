package com.brucefan.practice.factory;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Administrator on 2016/1/16.
 */
public interface Factory<T> {
    public <T> T getById(Long id);
}
