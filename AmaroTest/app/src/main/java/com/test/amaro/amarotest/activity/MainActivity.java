package com.test.amaro.amarotest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.adapter.RecyclerViewAdapter;
import com.test.amaro.amarotest.rest.APIClient;
import com.test.amaro.amarotest.rest.APIInterface;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.model.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    APIInterface apiInterface;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        /**
         GET List Resources
         **/
        Call<ProductResponse> call = apiInterface.doGetProductList();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                //Log.d("TAG",response.code()+"");

                ProductResponse productResponse = response.body();
                List<Product> productList = productResponse.products;
                loadList(productList);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                call.cancel();
            }
        });


    }

    private void loadList(List<Product> productList) {
        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerViewLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(context, productList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
