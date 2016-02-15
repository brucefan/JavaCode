package com.brucefan.code.okHttp;

import com.brucefan.code.concurrent.SynchronzedVsAtomic;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;

/**
 * Created by bruce01.fan on 2016/2/15.
 */
public class GetExample {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://www.163.com/").build();

        Response resposne = client.newCall(request).execute();

        if (!resposne.isSuccessful()) {
            throw new IOException("服务器错误:" + resposne);
        }

        Headers reponseHeaders = resposne.headers();
        for (int i = 0; i < reponseHeaders.size(); i++) {
            System.out.println(reponseHeaders.name(i) + " :: " + reponseHeaders.value(i));
        }

        //System.out.println(resposne.body().string());
    }
}
