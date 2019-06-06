package com.kjw.twentyhour.view;

import android.app.*;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;


import com.google.gson.internal.LinkedTreeMap;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.adapter.FoodViewAdapter;
import com.kjw.twentyhour.fragment.SelectedMenuListFragment;
import com.kjw.twentyhour.model.OrderSheet;
import com.kjw.twentyhour.model.Product;
import com.kjw.twentyhour.model.Response;
import com.kjw.twentyhour.network.NetworkUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.io.*;

import java.util.ArrayList;
import java.util.List;


public class MenuSelectionView extends AppCompatActivity {

    private static final String TAG = "MenuSelectionView";
    private static final String CHANNEL_ID = "내꺼야";
    private List<Product> noSelectedMenuData;
    private List<Product> selectedMenuData;
    private Object data;

    int overlapPosition;
    int currentSelectedMenuData;
    int clickMenuDataPosition;
    int totalprice = 0;
    Bundle bundle;

    FragmentTransaction fragmentTransaction;
    SelectedMenuListFragment selectedMenuListFragment;

    private OrderSheet orderSheet;
    private ListView listView;
    private TextView menuItemPrice;
    private TextView menuItemName;
    private TextView totalPriceTextView;
    private Button btOrderConfirm;
    private ProgressBar progressBar;

    private CompositeSubscription mSubscription;
    private SharedPreferences mPreference;
    private String name;
    public Product product;
    private boolean isOverlap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_selection);
        initalize();
        initNetwork();
        initView();
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        name = mPreference.getString("email", "");
        btOrderConfirm.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton(R.string.ok, (dialog, id) -> {
                postOrder(orderSheet);
                createNotificationChannel();
                notificationInvoke();

            });
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> dismissDialog(id));

            AlertDialog dialog = builder.create();
            dialog.show();



        });
    }


    private void notificationInvoke() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_favorite_24px)
                .setContentTitle("스타벅스")
                .setContentText("고객님 빠르게 준비하겠습니다.")
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(NotificationCompat.DEFAULT_SOUND);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(777, mBuilder.build());
    }

    private void postOrder(OrderSheet orderSheet) {
        mSubscription.add(
                NetworkUtil.getRetrofit().order(orderSheet)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void initalize() {
        noSelectedMenuData = new ArrayList<>();
        selectedMenuData = new ArrayList<>();
        orderSheet = new OrderSheet();
    }


    private void initNetwork() {
        mSubscription = new CompositeSubscription();
        mSubscription.add(NetworkUtil.getRetrofit().getProduct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void initView() {
        totalPriceTextView = (TextView) findViewById(R.id.total_price_text_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_menu);
        btOrderConfirm = (Button) findViewById(R.id.btn_order_confirm);
    }

    private void handleResponse(Product[] product) {
        progressBar.setVisibility(View.VISIBLE);
        List<Bitmap> bmpArray = new ArrayList<>();
        for (int o = 0; product.length > o; o++) {
            data = product[o].getImg().getData();
            LinkedTreeMap<String, ArrayList<Double>> listLinkedTreeMap = (LinkedTreeMap) data;
            ArrayList<Double> bitmapArray = listLinkedTreeMap.get("data");

            int h = bitmapArray.size();
            byte[] f = new byte[h];
            for (int i = 0; h > i; i++) {

                double g = (double) bitmapArray.get(i);
                int k = (int) g;
                f[i] = (byte) k;

            }
            byte[] a = f;
            Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, bitmapArray.size());
            bmpArray.add(bitmap);
            noSelectedMenuData.add(product[o]);

//            noSelectedListViewInitialize(bmpArray);

        }
        noSelectedListViewInitialize(bmpArray);
    }

    private void handleError(Throwable e) {


    }

    private void noSelectedListViewInitialize(List<Bitmap> bmpArray) {
        listViewAdapterSetting(bmpArray);
        listView.setOnItemClickListener((parent, menuView, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons
            builder.setPositiveButton(R.string.ok, (dialog, id1) -> {
                menuViewInit(menuView);
                productSetting(position);
                totalPriceCal();
                orderSheetSetting();
                startSelectedMenuListFragment();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, id12) -> {

            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void listViewAdapterSetting(List<Bitmap> bmpArray) {
        FoodViewAdapter foodViewAdapter = new FoodViewAdapter(this, R.layout.not_selected_menu_item, noSelectedMenuData, bmpArray);
        listView = findViewById(R.id.foodlist);
        listView.setAdapter(foodViewAdapter);
        foodViewAdapter.notifyDataSetChanged();
    }

    private void menuViewInit(View view) {
        menuItemPrice = view.findViewById(R.id.tv_menu_item_price);
        menuItemName = view.findViewById(R.id.tv_menu_item_name);
    }

    private void productSetting(int k) {

        checkOverlapPostion();
        product = new Product();

        if (isOverlap == true) {
            product = selectedMenuData.get(overlapPosition);
            selectedMenuData.get(overlapPosition).setQuantity(1);

        } else {
            product.setPrice(menuItemPrice.getText().toString());
            product.setProduct(menuItemName.getText().toString());
            product.setQuantity(1);
            selectedMenuData.add(product);
        }


    }

    private void checkOverlapPostion() {


        for (int i = 0; selectedMenuData.size() > i; i++) {
            if (selectedMenuData.get(i).getProduct() == menuItemName.getText().toString()) {
                isOverlap = true;
                overlapPosition = i;
                return;
            } else {
                isOverlap = false;
            }
        }

    }

    private void totalPriceCal() {
        totalprice += Integer.parseInt(product.getPrice());
        totalPriceTextView.setText("총합계: " + String.valueOf(totalprice));
    }

    private void orderSheetSetting() {

        orderSheet.name = name;
        orderSheet.totalPrice = totalprice;
        orderSheet.product = selectedMenuData;
//        orderSheet.product.add(product);
    }

    private void startSelectedMenuListFragment() {
        bundle = new Bundle();
        bundle.putSerializable("selectedMenuData", (Serializable) selectedMenuData);
        selectedMenuListFragment = new SelectedMenuListFragment();
        selectedMenuListFragment.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.calculator, selectedMenuListFragment);
        fragmentTransaction.commit();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "내꼬야";
            String description = "라비앙로즈";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }
}
