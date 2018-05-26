package com.test.amaro.amarotest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.adapter.RecyclerViewAdapter;
import com.test.amaro.amarotest.fragment.ProductDetailFragment;
import com.test.amaro.amarotest.rest.APIClient;
import com.test.amaro.amarotest.rest.APIInterface;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.model.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterrOnClickHandler,
        ProductDetailFragment.OnFragmentInteractionListener{

    APIInterface apiInterface;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    FrameLayout product_detail;
    ProductDetailFragment productDetailFragment = null;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        //TODO 1 check for internet conection
        //TODO 2 implement progress bar while fetching info from rest call
        //TODO 3 repository pattern would be great here
        Call<ProductResponse> call = apiInterface.doGetProductList();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
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

        recyclerViewAdapter = new RecyclerViewAdapter(context, productList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onClick(Product product) {
        if (product_detail == null){
            product_detail = (FrameLayout) findViewById(R.id.product_detail);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (productDetailFragment == null) {
            productDetailFragment = ProductDetailFragment.newInstance(product);
            fragmentTransaction.add(R.id.product_detail, productDetailFragment);
            fragmentTransaction.commit();
        } else {
            productDetailFragment.setProduct(product);
        }
        product_detail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentClose() {
        if (product_detail == null){
            product_detail = (FrameLayout) findViewById(R.id.product_detail);
        }
        product_detail.setVisibility(View.GONE);
    }
}