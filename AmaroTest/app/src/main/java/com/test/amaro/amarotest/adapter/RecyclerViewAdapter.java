package com.test.amaro.amarotest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        public TextView textView;
        public ViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.textview);
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
        viewHolder.textView.setText(productList.get(position).name);
    }

    @Override
    public int getItemCount(){
        return productList.size();
    }
}
