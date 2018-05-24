package com.test.amaro.amarotest.rest;

/**
 * Created by cfage on 23/05/2018.
 */

import com.test.amaro.amarotest.model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("59b6a65a0f0000e90471257d")
    Call<ProductResponse> doGetProductList();

}