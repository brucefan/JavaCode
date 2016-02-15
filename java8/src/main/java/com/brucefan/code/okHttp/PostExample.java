package com.brucefan.code.okHttp;

import okhttp3.*;

import java.io.IOException;

/**
 * Created by bruce01.fan on 2016/2/15.
 */
public class PostExample {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType TEXT = MediaType.parse("text/plain"); // 文本方式
        String postBody = "Hello World";

        Request request = new Request.Builder()
                .url("http://www.163.com")
                .post(RequestBody.create(TEXT, postBody))
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("服务器错误:" + response);
        }

        System.out.println(response.body().string());
    }
}
