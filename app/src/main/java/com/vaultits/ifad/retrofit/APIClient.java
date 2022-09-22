package com.vaultits.ifad.retrofit;

import com.vaultits.ifad.urls.HostServerAddress;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class APIClient {

    //public static final String BASE_URL = HostServerAddress.BASE_URL();//hotspot
    //public static final String BASE_URL = HostServerAddress.BASE_NET_URL();//office
    public static final String BASE_URL = HostServerAddress.BASE_SERVER_URL();//server
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(30,TimeUnit.MINUTES)
                .writeTimeout(30,TimeUnit.MINUTES)
                .build();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
