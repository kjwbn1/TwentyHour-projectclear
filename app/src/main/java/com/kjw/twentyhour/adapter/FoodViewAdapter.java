package com.kjw.twentyhour.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjw.twentyhour.R;
import com.kjw.twentyhour.data.Food;
import com.kjw.twentyhour.model.Product;
import com.kjw.twentyhour.network.NetworkUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class FoodViewAdapter extends BaseAdapter {

    private List<Product> products;
    private List<Bitmap> bitmaps;
    private LayoutInflater inflater;
    private int layout;


    public FoodViewAdapter(Context context, int layout, List<Product> products, List<Bitmap> bitmaps) {

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.products = products;
        this.bitmaps = bitmaps;
    }


    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
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

        Product product = products.get(position);
        Bitmap bitmap = bitmaps.get(position);

        ImageView imgMenuItem = (ImageView) convertView.findViewById(R.id.img_menu_item);
        imgMenuItem.setScaleType(ImageView.ScaleType.FIT_XY);
        imgMenuItem.setAdjustViewBounds(true);
        imgMenuItem.setImageBitmap(bitmap);

        TextView tvMenuItemPrice = (TextView) convertView.findViewById(R.id.tv_menu_item_price);
        tvMenuItemPrice.setText(product.getPrice());

        TextView tvMenuItemName = (TextView) convertView.findViewById(R.id.tv_menu_item_name);
        tvMenuItemName.setText(product.getProduct());

        TextView tvMenuDescription = (TextView) convertView.findViewById(R.id.tv_menu_description);
        tvMenuDescription.setText(product.getDescription());

        return convertView;
    }
}
