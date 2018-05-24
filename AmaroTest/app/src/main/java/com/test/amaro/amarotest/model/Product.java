package com.test.amaro.amarotest.model;

/**
 * Created by cfage on 23/05/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("style")
    @Expose
    public String style;

    @SerializedName("code_color")
    @Expose
    public String codeColor;

    @SerializedName("color_slug")
    @Expose
    public String colorSlug;

    @SerializedName("color")
    @Expose
    public String color;

    @SerializedName("on_sale")
    @Expose
    public Boolean onSale;

    @SerializedName("regular_price")
    @Expose
    public String regularPrice;

    @SerializedName("actual_price")
    @Expose
    public String actualPrice;

    @SerializedName("discount_percentage")
    @Expose
    public String discountPercentage;

    @SerializedName("installments")
    @Expose
    public String installments;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("sizes")
    @Expose
    public List<Size> sizes = null;

}
