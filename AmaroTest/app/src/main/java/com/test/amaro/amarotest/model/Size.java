package com.test.amaro.amarotest.model;

/**
 * Created by cfage on 23/05/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Size {

    @SerializedName("available")
    @Expose
    public Boolean available;

    @SerializedName("size")
    @Expose
    public String size;

    @SerializedName("sku")
    @Expose
    public String sku;

}