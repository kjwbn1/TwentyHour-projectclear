package com.kjw.twentyhour.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.adapter.TabPageAdapter;
import com.kjw.twentyhour.data.Time;
import com.kjw.twentyhour.model.Response;
import com.kjw.twentyhour.model.Status;
import com.kjw.twentyhour.network.NetworkUtil;
import com.kjw.twentyhour.utils.Constants;
import com.kjw.twentyhour.view.MenuSelectionView;
import com.kjw.twentyhour.view.NmapActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.app.Activity.RESULT_OK;

import static com.kjw.twentyhour.utils.Validation.validateFields;

public class StateFragment extends Fragment {






    public static final String TAG = StateFragment.class.getSimpleName();

    private static final int TAKE_TO_PRAYGRROUND_RESULT = 0;
    private static final int TAKE_TO_DAY_START = 1;
    private static final int TAKE_TO_TIME_START = 2;
    private static final int TAKE_TO_TIME_END = 3;


    private FrameLayout calendarFrame;
    private Button searchGround;
    public  Button dayStart;
    private Button timeStart;
    private Button gameSelect;
    private Button selectionComplete;
    TimePicker timePickerS;
    TimePicker timePickerE;

    String strDate;
    String endDate;

    private String mToken;

    String mapDataTemp;
    Context context;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SharedPreferences mSharedPreferences;
    private CompositeSubscription mSubscriptions;
    SharedPreferences.Editor editor;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state,container,false);
        context = getActivity();
        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();
        initViews(view);
        TabViewSetting();
        initClickButton();

        return view;
    }

    private void TabViewSetting() {
        tabLayout.addTab(tabLayout.newTab().setText("시작시간"));
        tabLayout.addTab(tabLayout.newTab().setText("종료시간"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        TabPageAdapter tabPageAdapter = new TabPageAdapter(getFragmentManager() , tabLayout.getTabCount());
        viewPager.setAdapter(tabPageAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    private void initClickButton() {
        searchGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NmapActivity.class);
                startActivityForResult(intent , TAKE_TO_PRAYGRROUND_RESULT);
            }

        });

        dayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction().addToBackStack("aa");
                CalendarFragment cf = new CalendarFragment();
                ft.replace(R.id.calendar_frame, cf, "cf");
                ft.commit();
                calendarFrame.setVisibility(View.VISIBLE);
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        });

        gameSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , MenuSelectionView.class);
                startActivity(intent);
            }
        });

        selectionComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Status status = new Status();
                status.setEmail(mSharedPreferences.getString("email", ""));
                status.setAddress(mSharedPreferences.getString("address",""));
                status.setDay(mSharedPreferences.getString("day" , ""));
                status.setTimeS(mSharedPreferences.getString("timestart",""));
                status.setTimeE(mSharedPreferences.getString("timeend",""));



                mSubscriptions.add(NetworkUtil.getRetrofit().statusRegister(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));




            }

            private void handleResponse(Response response) {
                Toast.makeText(getActivity(),response.getMessage(),Toast.LENGTH_LONG).show();

            }

            private void handleError(Throwable error) {

                if (error instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) error).response().errorBody().string();
                        Response response = gson.fromJson(errorBody,Response.class);
                        Toast.makeText(getActivity(),response.getMessage(),Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getActivity(),"Network Error !", Toast.LENGTH_LONG).show();

                }

            }

        });


    }


    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = mSharedPreferences.edit();
    }

    public void setDate(String date){

        dayStart.setText(date);
        calendarFrame.setVisibility(View.GONE);
        editor.putString("day" , date);
        editor.commit();


    };

    public void setTime(Time time){

        SimpleDateFormat sdfDate = new SimpleDateFormat( "HH시mm분");//dd/MM/yyyy
        Date now = new Date(0,0,0, time.getHour(),time.getMin());
        Date future = new Date(0, 0, 0, time.getHourE(), time.getMinE());
        strDate = sdfDate.format(now);
        endDate = sdfDate.format(future);


        editor.putString("timestart" , strDate);
        editor.putString("timeend" , endDate);
        editor.commit();

        timeStart.setText(strDate + "에서 " + endDate + "까지");
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);

    }

    public void transTab(){
        tabLayout.getTabAt(1).select();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == TAKE_TO_PRAYGRROUND_RESULT) {
                if (data.hasExtra("address")) {
                    mapDataTemp = data.getStringExtra("address");
                    searchGround.setText(mapDataTemp);
                    editor.putString("address" , mapDataTemp);
                    editor.commit();
                }
            }

            if(requestCode == TAKE_TO_DAY_START) {
                mapDataTemp = data.getStringExtra("daystart");
                dayStart.setText(mapDataTemp);

                editor.putString("daystart" , mapDataTemp);
                editor.apply();

            }


            if(requestCode == TAKE_TO_TIME_START) {
                mapDataTemp = data.getStringExtra("timestart");
                timeStart.setText(mapDataTemp);
            }

            if(requestCode == TAKE_TO_TIME_END) {
                mapDataTemp = data.getStringExtra("timeend");
                timeStart.setText(mapDataTemp);
            }
        }
    }


    public void initViews(View v) {
        searchGround = (Button) v.findViewById(R.id.btn_search);
        dayStart = (Button) v.findViewById(R.id.btn_day);
        timeStart = (Button) v.findViewById(R.id.btn_time_s);
        gameSelect = (Button) v.findViewById(R.id.btn_game_select);
        selectionComplete = (Button) v.findViewById(R.id.btn_select_complete);
        calendarFrame = (FrameLayout) v.findViewById(R.id.calendar_frame);
        timePickerS = (TimePicker) v.findViewById(R.id.timepicker_start);
        timePickerE = (TimePicker) v.findViewById(R.id.timepicker_end);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tablayout);


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");

        if(!validateFields(mToken)){



        }else{

            searchGround.setText(mSharedPreferences.getString("address",""));
            dayStart.setText(mSharedPreferences.getString("day",""));
            timeStart.setText(mSharedPreferences.getString("timestart","") +"에서" + mSharedPreferences.getString("timeend", ""));
            gameSelect.setText("바둑");

        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.commit();
        mSubscriptions.unsubscribe();

    }

    //    private void setMargins (View view, int left, int top, int right, int bottom, int start ,int end) {
//        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//            p.setMargins(left, top, right, bottom);
//            p.setMarginStart(start);
//            p.setMarginEnd(end);
//
//            view.requestLayout();
//        }
//    }

}
