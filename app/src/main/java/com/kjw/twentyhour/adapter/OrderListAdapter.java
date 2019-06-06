package com.kjw.twentyhour.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.kjw.twentyhour.databinding.OrderItemBinding;
import com.kjw.twentyhour.model.NewOrderSheet;
import com.kjw.twentyhour.model.NewProduct;
import com.kjw.twentyhour.model.Product;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {

    private List<NewOrderSheet> messageList;
    private int layout;
    private LayoutInflater inflater;
    private List<NewProduct> newProductList;

    OrderItemBinding binding;

    public OrderListAdapter(Context context, int layout, List<NewProduct> products) {
        this.inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.messageList = messageList;
        this.layout = layout;
        this.newProductList = products;
    }

    @Override
    public int getCount() {
        return newProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return newProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =  inflater.inflate(layout,parent,false);
        }

        binding = DataBindingUtil.bind(convertView);




        String price = newProductList.get(position).price;
        String productName = newProductList.get(position).product;
        String productQuantity = newProductList.get(position).quantity;
        String productPriceForQuantity  = String.valueOf(Integer.valueOf(price) * Integer.valueOf(productQuantity));

        binding.tvPricePerProduct.setText(price);
        binding.tvProductName.setText(productName);
        binding.tvProductQuantity.setText(productQuantity);
        binding.tvProductPrice.setText(productPriceForQuantity);


        return convertView;

    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item , parent  , false );
//
//        return new OrderListAdapter.ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        String price = messageList.get(position).newProductList.get(position).price;
//        String productName = messageList.get(position).newProductList.get(position).product;
//        String productQuantity = messageList.get(position).newProductList.get(position).quantity;
//        String productPriceForQuantity  = String.valueOf(Integer.valueOf(price) * Integer.valueOf(productQuantity));
//
//        holder.binding.tvPricePerProduct.setText(price);
//        holder.binding.tvProductName.setText(productName);
//        holder.binding.tvProductQuantity.setText(productQuantity);
//        holder.binding.tvProductPrice.setText(productPriceForQuantity);
//    }
//
//    @Override
//    public int getItemCount() {
//        return messageList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        OrderItemBinding  binding;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            binding = DataBindingUtil.bind(itemView);
//        }
//    }


}
