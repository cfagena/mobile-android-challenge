package com.test.amaro.amarotest.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.model.Size;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment implements View.OnClickListener{
    private static final String PRODUCT = "PRODUCT";

    private Product product;
    private TextView productName;
    private TextView productRegularPrice;
    private TextView productActualPrice;
    private TextView productOnSale;
    private TextView productSizeLabel;
    private LinearLayout sizesContainer;

    private Button closeButton;
    private ImageView productImageView;
    private Context context;

    private OnFragmentInteractionListener mListener;

    public ProductDetailFragment() {
    }

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(PRODUCT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);
        productName = (TextView) rootView.findViewById(R.id.product_detail_name);
        productRegularPrice = (TextView) rootView.findViewById(R.id.product_detail_regprice);
        productActualPrice = (TextView) rootView.findViewById(R.id.product_detail_actualprice);
        productOnSale = (TextView) rootView.findViewById(R.id.product_detail_onsale);
        productImageView = (ImageView) rootView.findViewById(R.id.product_detail_image);
        productSizeLabel = (TextView) rootView.findViewById(R.id.product_detail_size_label);
        sizesContainer = (LinearLayout ) rootView.findViewById(R.id.sizes_container);
        updateProductFields(product);

        closeButton = (Button) rootView.findViewById(R.id.bt_close_fragment);
        closeButton.setOnClickListener(this);

        return rootView;
    }

    public void setProduct(Product product) {
        this.product = product;
        updateProductFields(product);
    }

    private void updateProductFields(Product product) {
        if (product.image == null || product.image.trim().isEmpty()) {
            Log.e("DetailFragment", product.name);
            Picasso.with(context).
                    load(R.mipmap.img_product_placeholder).
                    into(productImageView);
        } else {
            Picasso.with(context).
                    load(product.image).
                    fit().
                    centerInside().
                    placeholder(R.mipmap.img_product_placeholder).
                    into(productImageView);
        }

        productName.setText(product.name);

        if (product.onSale){
            productRegularPrice.setVisibility(View.VISIBLE);
            productRegularPrice.setText(product.regularPrice);
            productRegularPrice.setPaintFlags(productRegularPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            productActualPrice.setText(product.actualPrice);
            productOnSale.setVisibility(View.VISIBLE);
            productOnSale.setText(getResources().getString(R.string.on_sale));

        } else {
            productRegularPrice.setVisibility(View.GONE);
            productOnSale.setVisibility(View.GONE);
            productOnSale.setText("");
            productActualPrice.setText(product.actualPrice);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.size_label));

        ArrayList<Size>availableSizes = getAvailableSizes(product.sizes);
        sizesContainer.removeAllViews();

        if (availableSizes != null && availableSizes.size() == 1){
            Size size = product.sizes.get(0);

            if (getResources().getString(R.string.one_size_fits_all).equals(size.size)){
                // one size fits all - tamanho unico
                stringBuilder.append(" ");
                stringBuilder.append(getResources().getString(R.string.one_size_fits_all));
                productSizeLabel.setText(stringBuilder.toString());
                updateSizeContainerWithOneSizeFitsAll();
            } else {
                productSizeLabel.setText(stringBuilder.toString());
                updateSizeContainer(availableSizes);
            }

        } else if (availableSizes != null && availableSizes.size() > 1){
            productSizeLabel.setText(stringBuilder.toString());
            updateSizeContainer(availableSizes);
        } else {
            // null or empty list
            productSizeLabel.setText(getResources().getString(R.string.no_available_sizes));
        }
    }

    private void updateSizeContainerWithOneSizeFitsAll() {
        TextView textView = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(layoutParams);
        textView.setText(getResources().getString(R.string.one_size_fits_all));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        textView.setGravity(Gravity.CENTER);
        textView.setVisibility(View.VISIBLE);
        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.one_size_shape));
        sizesContainer.addView(textView);
    }

    private void updateSizeContainer(ArrayList<Size> availableSizes) {
        for (Size size: availableSizes) {
            //TODO cache those TextViews
            //TODO extend TextView to customize
            TextView textView = new TextView(context);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            textView.setText(size.size);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            textView.setTextColor(ContextCompat.getColor(context, R.color.black));
            textView.setGravity(Gravity.CENTER);
            textView.setVisibility(View.VISIBLE);
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.size_shape));
            sizesContainer.addView(textView);
        }
    }

    private ArrayList<Size> getAvailableSizes(List<Size> sizes) {
        ArrayList<Size> temporarySizeList = new ArrayList<Size>();
        if (sizes != null && sizes.size() > 0){
            for (Size size: sizes) {
                if (size.available) temporarySizeList.add(size);
            }
            return new ArrayList<Size>(temporarySizeList);
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_close_fragment:
                mListener.onFragmentClose();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentClose();
    }
}
