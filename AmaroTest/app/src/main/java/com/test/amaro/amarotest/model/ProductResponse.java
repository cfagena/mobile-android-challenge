package com.test.amaro.amarotest.model;

/**
 * Created by cfage on 23/05/2018.
 */

import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ProductResponse {

    @SerializedName("products")
    @Expose
    public List<Product> products = null;

}
