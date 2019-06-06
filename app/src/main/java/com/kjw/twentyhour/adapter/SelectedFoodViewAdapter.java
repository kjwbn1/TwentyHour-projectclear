package com.kjw.twentyhour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kjw.twentyhour.R;
import com.kjw.twentyhour.model.OrderSheet;
import com.kjw.twentyhour.model.Product;
import com.kjw.twentyhour.model.Response;
import com.kjw.twentyhour.network.NetworkUtil;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;


public class SelectedFoodViewAdapter extends BaseAdapter {

    private List<Product> selectedMenuData;
    private int layout;
    private LayoutInflater inflater;

    private Button orderConfirm;
    public  OrderSheet orderSheet;


    public SelectedFoodViewAdapter(Context context, int layout, List<Product> selectedMenuData) {

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedMenuData = selectedMenuData;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return selectedMenuData.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedMenuData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        Product product = selectedMenuData.get(position);


//        orderConfirm = parent.findViewById(R.id.btn_order_confirm);
//        orderConfirm.setOnClickListener(v -> {
//
//            postOrder(orderSheet);
//
//
//        });


        TextView price = (TextView) convertView.findViewById(R.id.selected_price);
        TextView foodName = (TextView) convertView.findViewById(R.id.selected_food_name);

        price.setText(product.getPrice());
        foodName.setText(product.getProduct());


        return convertView;
    }









}
