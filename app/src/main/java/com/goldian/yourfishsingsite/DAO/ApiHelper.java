package com.goldian.yourfishsingsite.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

//    main wifi
//    public static final String BASE_URL="http://192.168.100.27:8082/CodeIgniter/fishingsite/api/";

//    secondary wifi
//        public static final String BASE_URL="http://192.168.42.123:8082/CodeIgniter/fishingsite/api/";

//    phone
    public static final String BASE_URL=  "http://192.168.43.146:8082/CodeIgniter/fishingsite/api/";

    public static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface apiInterface(){
        return getClient().create(ApiInterface.class);
    }
}
