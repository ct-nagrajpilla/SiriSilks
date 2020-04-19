package com.aniapps.callbackclient;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by NagRaj_Pilla on 4/13/2017.
 * Service call
 * https://inthecheesefactory.com/blog/retrofit-2.0/en
 * https://github.com/JessYanCoding/RetrofitUrlManager
 * https://futurestud.io/tutorials/retrofit-2-how-to-use-dynamic-urls-for-requests
 * https://gist.github.com/laaptu/feb3cae85f07c0c5fe3e
 * *****https://stackoverflow.com/questions/27399633/how-to-address-multiple-api-end-points-using-retrofit
 * https://github.com/macieknajbar/MockServer/blob/master/app/src/main/java/com/example/mockserver/rest/server/MockServer.kt
 */

public interface APIService {
    @FormUrlEncoded
    @POST
    Call<String> getApiResult(@Url String url, @FieldMap Map<String, String> fields);

    @Multipart
    @POST
    Call<String> uploadImage(@Url String baseUrl, @Part MultipartBody.Part file, @PartMap() Map<String, String> fields);
}
