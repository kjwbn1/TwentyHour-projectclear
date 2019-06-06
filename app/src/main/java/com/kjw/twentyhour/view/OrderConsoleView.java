package com.kjw.twentyhour.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kjw.twentyhour.OwnerConsoleActivity;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.adapter.ChatAdapter;
import com.kjw.twentyhour.data.Message;
import com.kjw.twentyhour.model.*;
import com.kjw.twentyhour.network.ChatApplication;
import com.kjw.twentyhour.utils.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.kjw.twentyhour.utils.Constants.CHAT_SERVER_URL;

public class OrderConsoleView extends AppCompatActivity {

    private String Nickname;
    private Socket mSocket;

    public RecyclerView myRecylerView;
    public List<NewOrderSheet> MessageList;
    public ChatAdapter chatBoxAdapter;
    public EditText messagetxt;
    public Button send;
    FullDocument fullDocument;
    NewProduct newProduct;

    Boolean isConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_console_view);
        try {
            mSocket = IO.socket(CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        messagetxt = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.btn_send);


        mSocket.on("userjoinedthechat", onUserJoinedTheChat);
        mSocket.on("userdisconnect", onUserDisconnect);
        mSocket.on("message", onMessage);
        mSocket.connect();


        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());


        // message send action
        send.setOnClickListener(v -> {
            if (!messagetxt.getText().toString().isEmpty()) {
                mSocket.emit("messagedetection", "닉네임", messagetxt.getText().toString());
            }
            messagetxt.setText(" ");
        });


    }

    private Emitter.Listener onUserJoinedTheChat = args -> runOnUiThread(() -> {
        String data = (String) args[0];
        Toast.makeText(OrderConsoleView.this, data, Toast.LENGTH_SHORT).show();
    });


    private Emitter.Listener onUserDisconnect = args -> runOnUiThread(() -> {
        String data = (String) args[0];
        Toast.makeText(OrderConsoleView.this, data, Toast.LENGTH_SHORT).show();
    });

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];


                String doc = data.toString();
                ObjectMapper mapper = new ObjectMapper();

                try {
                    fullDocument = mapper.readValue(doc, FullDocument.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String employee = fullDocument.employee;
                String uploadDate = fullDocument.uploadDate;
                List<NewProduct> products = fullDocument.product;
                String totalPrice = fullDocument.totalPrice;

                NewOrderSheet newOrderSheet = new NewOrderSheet(products, employee, uploadDate, totalPrice);

                MessageList.add(newOrderSheet);

                chatBoxAdapter = new ChatAdapter(MessageList);



                myRecylerView.setAdapter(chatBoxAdapter);


            });
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("userjoinedthechat", onUserJoinedTheChat);
        mSocket.off("userdisconnect", onUserDisconnect);
        mSocket.off("message", onMessage);
        mSocket.off();
        mSocket.disconnect();


    }
}

