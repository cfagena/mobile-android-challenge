package com.test.amaro.amarotest.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.model.Size;

import java.util.ArrayList;

/**
 * Created by cfage on 27/05/2018.
 */

public class SizeContainerView extends LinearLayout {

    private ArrayList<Size> availableSizes;
    private ArrayList<TextView> sizeTextViewList;
    private TextView uSizeTextView;

    public SizeContainerView(Context context) {
        super(context);
        init();
    }

    public SizeContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SizeContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SizeContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        uSizeTextView = (TextView) inflate(getContext(), R.layout.u_size_text_view, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        addView(uSizeTextView, layoutParams);
        uSizeTextView.setVisibility(GONE);
        sizeTextViewList = new ArrayList<TextView>();
    }

    public ArrayList<Size> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(ArrayList<Size> availableSizes) {
        this.availableSizes = availableSizes;
        updateView();
    }

    private void updateView() {
        if (availableSizes != null) {
            if (availableSizes.size() == 1) {
                Size size = availableSizes.get(0);
                if (getResources().getString(R.string.one_size_fits_all).equals(size.size.trim().toUpperCase())){
                    uSizeTextView.setVisibility(VISIBLE);
                    setSizeTextViewListGone(0);
                } else {
                    updateSizeTextViewList();
                }
            } else {
                updateSizeTextViewList();
            }
        }
    }

    private void setSizeTextViewListGone(int startIndex){
        if (sizeTextViewList != null){
            for (int i = startIndex; i < sizeTextViewList.size(); i++){
                TextView textView = sizeTextViewList.get(i);
                textView.setVisibility(GONE);
            }
        }
    }

    private void updateSizeTextViewList(){
        uSizeTextView.setVisibility(GONE);
        int index = 0;
        ArrayList<TextView> temporaryList = new ArrayList<>();
        TextView textView;

        for (Size size : availableSizes) {
            if (index >= sizeTextViewList.size()) {
                textView = (TextView) inflate(getContext(), R.layout.size_text_view, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                addView(textView, layoutParams);
                temporaryList.add(textView);
            } else {
                textView = sizeTextViewList.get(index);
            }
            textView.setText(size.size);
            textView.setVisibility(VISIBLE);
            index++;
        }

        if (sizeTextViewList.size() > index) {
            setSizeTextViewListGone(index);
        }
    }
}
