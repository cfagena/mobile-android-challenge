package com.test.amaro.amarotest.model;

/**
 * Created by cfage on 23/05/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {

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

    private int actualPriceInt;

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

    public int getActualPriceInt() {
        if (actualPriceInt == -1){
            if (actualPrice != null && actualPrice.trim().length() > 0){
                String price = actualPrice.replaceAll("[^\\d]+", "");
                actualPriceInt = Integer.parseInt(price);
            }
        }
        return actualPriceInt;
    }

    public Product(){
        actualPriceInt = -1;
    }

    protected Product(Parcel in) {
        name = in.readString();
        style = in.readString();
        codeColor = in.readString();
        colorSlug = in.readString();
        color = in.readString();
        byte onSaleVal = in.readByte();
        onSale = onSaleVal == 0x02 ? null : onSaleVal != 0x00;
        regularPrice = in.readString();
        actualPrice = in.readString();
        actualPriceInt = in.readInt();
        discountPercentage = in.readString();
        installments = in.readString();
        image = in.readString();
        if (in.readByte() == 0x01) {
            sizes = new ArrayList<Size>();
            in.readList(sizes, Size.class.getClassLoader());
        } else {
            sizes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(style);
        dest.writeString(codeColor);
        dest.writeString(colorSlug);
        dest.writeString(color);
        if (onSale == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (onSale ? 0x01 : 0x00));
        }
        dest.writeString(regularPrice);
        dest.writeString(actualPrice);
        dest.writeInt(actualPriceInt);
        dest.writeString(discountPercentage);
        dest.writeString(installments);
        dest.writeString(image);
        if (sizes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(sizes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static Comparator<Product> priceComparator = new Comparator<Product>() {
        public int compare(Product product1, Product product2) {
            return Integer.compare(product1.getActualPriceInt(), product2.getActualPriceInt());
        }};
}