package com.kjw.twentyhour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjw.twentyhour.R;
import com.kjw.twentyhour.data.Food;

import java.util.List;

public class FoodViewAdapter extends BaseAdapter {

    private List<Food> foodData;
    private int layout;
    private LayoutInflater inflater;




    public  FoodViewAdapter(Context context , int layout, List<Food> foodData)
    {

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.foodData = foodData;
        this.layout = layout;

    }







    @Override
    public int getCount() {
        return foodData.size();
    }

    @Override
    public Object getItem(int position) {
        return foodData.get(position).getDescription();
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

        Food food = foodData.get(position);


        ImageView foodImage = (ImageView) convertView.findViewById(R.id.food);
        foodImage.setImageResource(food.getFoodImage());

        TextView price = (TextView) convertView.findViewById(R.id.price);

        price.setText(food.getPrice());

        TextView foodName = (TextView) convertView.findViewById(R.id.food_name);
        foodName.setText(food.getFoodName());

        TextView descrption = (TextView) convertView.findViewById(R.id.food_description);
        descrption.setText(food.getDescription());

        return convertView;
    }
}
