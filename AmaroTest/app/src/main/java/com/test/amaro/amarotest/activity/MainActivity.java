package com.test.amaro.amarotest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

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
        ProductDetailFragment.OnFragmentInteractionListener{

    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private FrameLayout product_detail;
    private ProductDetailFragment productDetailFragment = null;
    private List<Product> productList;
    private MenuItem showOnSaleItem;
    private MenuItem sortByLowestPriceItem;

    private List<Product> hiddenProductList;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        //TODO 1 check for internet conection
        //TODO 2 implement progress bar while fetching info from rest call
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        showOnSaleItem = menu.findItem(R.id.action_show_onsale);
        sortByLowestPriceItem = menu.findItem(R.id.action_sort_low_price);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_low_price) {
            Collections.sort(productList, Product.priceComparator);
            ((RecyclerViewAdapter)recyclerViewAdapter).setNewProductList(productList);

            return true;
        } else if (id == R.id.action_show_onsale) {
            if (showOnSaleItem.isChecked()){
                showOnSaleItem.setChecked(false);
                showAllProducts();
                ((RecyclerViewAdapter)recyclerViewAdapter).setNewProductList(productList);
            } else {
                showOnSaleItem.setChecked(true);
                leftOnSaleProductsOnly();
                ((RecyclerViewAdapter)recyclerViewAdapter).setNewProductList(productList);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}