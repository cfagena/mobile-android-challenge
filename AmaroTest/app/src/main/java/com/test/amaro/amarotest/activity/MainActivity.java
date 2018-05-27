package com.test.amaro.amarotest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.adapter.RecyclerViewAdapter;
import com.test.amaro.amarotest.fragment.ProductDetailFragment;
import com.test.amaro.amarotest.rest.APIClient;
import com.test.amaro.amarotest.rest.APIInterface;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.model.ProductResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterrOnClickHandler,
        ProductDetailFragment.OnFragmentInteractionListener, View.OnClickListener{

    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private FrameLayout product_detail;
    private ProductDetailFragment productDetailFragment = null;
    private List<Product> productList;
    private ProgressBar mProgressBar;

    private TextView filterTextButton;
    private TextView sortTextButton;
    private Boolean onSaleFilterEnabled;

    private List<Product> hiddenProductList;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        filterTextButton = (TextView) findViewById(R.id.filter_text_button);
        sortTextButton =  (TextView) findViewById(R.id.sort_text_button);
        onSaleFilterEnabled = false;

        filterTextButton.setOnClickListener(this);
        sortTextButton.setOnClickListener(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        //TODO Splash screen
        //TODO 1 check for internet conection
        //TODO 3 repository pattern would be great here
        Call<ProductResponse> call = apiInterface.doGetProductList();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ProductResponse productResponse = response.body();
                productList = productResponse.products;
                loadRecyclerView();
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }

    private void loadRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerViewLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(context, productList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        mProgressBar.setVisibility(View.GONE);
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
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Bundle args = new Bundle();
            args.putParcelable(ProductDetailFragment.PRODUCT, product);
            productDetailFragment.setArguments(args);
            fragmentTransaction.replace(R.id.product_detail, productDetailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        product_detail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        if (product_detail == null){
            product_detail = (FrameLayout) findViewById(R.id.product_detail);
        }
        product_detail.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentClose() {
        onBackPressed();
    }

    private void showAllProducts() {
        if ((hiddenProductList != null) && !hiddenProductList.isEmpty()){
            productList.addAll(hiddenProductList);
            hiddenProductList.clear();
        }
    }

    private void leftOnSaleProductsOnly(){
        ArrayList<Product> temporary = new ArrayList<Product>();
        for (Product product: productList) {
            if (!product.onSale) {
                temporary.add(product);
            }
        }
        if (hiddenProductList == null) {
            hiddenProductList  = new ArrayList<Product>();
        }
        productList.removeAll(temporary);
        hiddenProductList.addAll(temporary);
    }

    // get rid of it
    private void printProductList(List<Product> productList){
        for (Product product: productList) {
            Log.d("", "Nome: " + product.name + " Preço: " + product.getActualPriceInt());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.sort_text_button){
            Collections.sort(productList, Product.priceComparator);
            ((RecyclerViewAdapter)recyclerViewAdapter).setNewProductList(productList);
            recyclerView.smoothScrollToPosition(0);
        } else if (id == R.id.filter_text_button){
            if (onSaleFilterEnabled){
                updateOnSaleTextButton();
                showAllProducts();
                ((RecyclerViewAdapter)recyclerViewAdapter).setNewProductList(productList);
            } else {
                updateOnSaleTextButton();
                leftOnSaleProductsOnly();
                ((RecyclerViewAdapter)recyclerViewAdapter).setNewProductList(productList);
            }
        }
    }

    private void updateOnSaleTextButton(){
        if (onSaleFilterEnabled){
            onSaleFilterEnabled = false;
            filterTextButton.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGray));
            filterTextButton.setTextColor(ContextCompat.getColor(context, R.color.darkGray));
        } else {
            onSaleFilterEnabled = true;
            filterTextButton.setBackgroundColor(ContextCompat.getColor(context, R.color.darkGray));
            filterTextButton.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }
}