package com.kjw.twentyhour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.model.NewOrderSheet;
import com.kjw.twentyhour.model.NewProduct;
import io.socket.client.IO;
import io.socket.client.Socket;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private List<NewOrderSheet> messageList;
    private List<NewProduct> products;
    Socket socket;
    Context context;


    public ChatAdapter(List<NewOrderSheet> messageList) {
        products = new ArrayList<>();


        this.messageList  = messageList;
        this.products = messageList.get(0).newProductList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context =  viewGroup.getContext();

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);

        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        OrderListAdapter orderListAdapter = new OrderListAdapter(context, R.layout.order_item, products );
        orderListAdapter.notifyDataSetChanged();
        viewHolder.listView.setAdapter(orderListAdapter);




    }

    private void recevieMessage(int i) {

        try {

            socket = IO.socket("http://10.0.2.2:8080");
            socket.connect();
            socket.emit("completeorder", "음식이 준비가 완료되었습니다.");
            socket.disconnect();
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        ListView listView;

        public ViewHolder(View view) {
            super(view);

            listView = (ListView)view.findViewById(R.id.list_orders);

        }
    }
}
