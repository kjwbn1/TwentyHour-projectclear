package com.kjw.twentyhour.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;


import com.kjw.twentyhour.R;

import java.text.SimpleDateFormat;


public class CalendarFragment extends Fragment {

    public interface onSetDateListener{

        void setDate(String ss);
    }

    public static final String TAG = RegisterFragment.class.getSimpleName();

    public Button selectDate;
    private CalendarView calendarView;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    private  onSetDateListener mListener;


    String selectedFormatedDate;
    Context context;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);

        context = getActivity();

        initSharedPreferences();
        initViews(view);


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YY년 M월 d일 (E)");
                selectedFormatedDate = simpleDateFormat.format(calendarView.getDate());
                mListener.setDate(selectedFormatedDate);

          }
      });

       return view;
   }



   private void initSharedPreferences() {


    }


   private void initViews(View v) {

         selectDate = (Button) v.findViewById(R.id.select_date);
         calendarView = (CalendarView)v.findViewById(R.id.calendar_view);
   }


   @Override
   public void onAttach(Context context){
        super.onAttach(context);

        mListener = (CalendarFragment.onSetDateListener)context;

    }

    @Override
   public void onDestroy() {
       super.onDestroy();

   }

}