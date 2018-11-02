package com.kjw.twentyhour.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.applikeysolutions.cosmocalendar.model.Day;

import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.kjw.twentyhour.R;


import java.text.SimpleDateFormat;
import java.util.List;


public class CalendarPickerView extends AppCompatActivity
{

    //View
    private CalendarView calendarView;
    private Toolbar myToolbar;


    private Button selectedDate;


    private String selectedDateDataStart;
//    private String selectedDateDataEnd;

    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";
    private Pair<Day, Day> days;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar);


        initViews();
        setText();



    }

    //            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)) {
//                String allPreviousLifecycleCallbacks = savedInstanceState
//                        .getString(LIFECYCLE_CALLBACKS_TEXT_KEY);
//                selectedDate.setText(allPreviousLifecycleCallbacks);
//            }
//        }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        String lifecycleDisplayTextViewContents = selectedDate.getText().toString();
//        outState.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, lifecycleDisplayTextViewContents);
//    }


    private void setText() {
        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Day> day = calendarView.getSelectedDays();
//                RangeSelectionManager rangeSelectionManager = (RangeSelectionManager) calendarView.getSelectionManager();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY.MM.dd");
//                days = rangeSelectionManager.getDays();
//
//
                try {
                    selectedDateDataStart = simpleDateFormat.format(day.get(0).getCalendar().getTime());
                    Intent data = new Intent();
                    data.putExtra("daystart", selectedDateDataStart);
                    setResult(RESULT_OK, data);
//                    selectedDateDataEnd = simpleDateFormat.format(days.second.getCalendar().getTime());
                } catch (NullPointerException e) {
//                    System.out.println(e);
//
                } finally {
                    finish();

                }

            }


        });



    }

    private void initViews() {
        calendarView = (CalendarView)findViewById(R.id.calendar_view);
        calendarView.setSelectionType(0);
        calendarView.setCalendarOrientation(0);
        selectedDate = (Button)findViewById(R.id.select_date);
//        myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);






        //RangeSelectionManager rangeSelectionManager = new RangeSelectionManager(this);


    }

}



