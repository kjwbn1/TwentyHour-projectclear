package com.kjw.twentyhour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kjw.twentyhour.R;
import com.kjw.twentyhour.data.Food;

import java.util.List;


public class SelectedFoodViewAdapter extends BaseAdapter {

    private List<Food> selectedFoodData;
    private int layout;
    private LayoutInflater inflater;


    public SelectedFoodViewAdapter(Context context , int layout, List<Food> selectedFoodData){

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedFoodData = selectedFoodData ;
        this.layout = layout;
    }





    @Override
    public int getCount() {
        return selectedFoodData.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedFoodData.get(position).getFoodName();
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

        Food food = selectedFoodData.get(position);


        TextView price = (TextView) convertView.findViewById(R.id.selected_price);

        price.setText(food.getPrice());

        TextView foodName = (TextView) convertView.findViewById(R.id.selected_food_name);
        foodName.setText(food.getFoodName());




        return convertView;


    }
}
