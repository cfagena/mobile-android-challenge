package com.test.amaro.amarotest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.model.Product;

import java.util.List;

/**
 * Created by cfage on 24/05/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<Product> productList;
    Context context;

    public RecyclerViewAdapter(Context context, List<Product> productList){
        this.productList = productList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView;
        public TextView priceTextView;
        public ImageView productImageView;

        public ViewHolder(View v){
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.nameTextView);
            priceTextView = (TextView) v.findViewById(R.id.priceTextView);
            productImageView = (ImageView) v.findViewById(R.id.productImageView);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        viewHolder.nameTextView.setText(productList.get(position).name);
        viewHolder.priceTextView.setText(productList.get(position).actualPrice);

        if (productList.get(position).image == null || productList.get(position).image.trim().isEmpty()) {
            Log.e("Adapter", productList.get(position).name);
            Picasso.with(context).
                    load(R.mipmap.img_product_placeholder).
                    into(viewHolder.productImageView);
        } else {
            Picasso.with(context).
                    load(productList.get(position).image).
                    placeholder(R.mipmap.img_product_placeholder).
                    into(viewHolder.productImageView);
        }
    }

    @Override
    public int getItemCount(){
        return productList.size();
    }
}
