package com.pavigeeth.alzarcapartment.Miscellaneous;

/**
 * Created by sehalsein on 12/11/17.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://10.0.0.205:3000/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
