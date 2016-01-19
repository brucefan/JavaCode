package com.brucefan.code.future;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 */
public class ListenableFutureTest {

    public static void main(String[] args) {
        List<String> sites = Lists.newArrayList("www.baidu.com");
        ListeningExecutorService pool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(8));
        for (String site : sites) {
            final ListenableFuture<String> future = pool.submit(new Callable() {
                public String call() throws Exception {
                    return printUrl(site);
                }
            });

            // 实现方式一
            future.addListener(new Runnable() {
                public void run() {
                    try {
                        String result = future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }, MoreExecutors.sameThreadExecutor());

            // 实现二
            Futures.addCallback(future, new FutureCallback<String>() {
                @Override
                public void onSuccess(String result) {
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });

            // 方案三
            ListenableFuture<String> listenableFuture =
                    JdkFutureAdapters.listenInPoolThread(future);
        }


    }

    static String printUrl(String url) throws InterruptedException {
        Thread.sleep(2000);
        return url;
    }
}
