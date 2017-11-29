package com.pavigeeth.alzarcapartment.Miscellaneous;

import com.pavigeeth.alzarcapartment.Model.NotificationDetail;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by sehalsein on 12/11/17.
 */

public interface APIService {
    @POST("/alzarc")
    @FormUrlEncoded
    Call<NotificationDetail> savePost(@Field("title") String title,
                                      @Field("message") String message,
                                      @Field("topic") String topic);
}
