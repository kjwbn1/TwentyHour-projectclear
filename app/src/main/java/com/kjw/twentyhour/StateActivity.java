package com.kjw.twentyhour;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.kjw.twentyhour.data.Time;
import com.kjw.twentyhour.dialogs.StoreSelectionDialog;
import com.kjw.twentyhour.fragment.CalendarFragment;
import com.kjw.twentyhour.fragment.ChangePasswordDialog;
import com.kjw.twentyhour.fragment.ProfileFragment;
import com.kjw.twentyhour.fragment.StateFragment;
import com.kjw.twentyhour.fragmenttofragmentinterface.FragmentDataSendInterface;
import com.kjw.twentyhour.helper.BottomNavigationViewHelper;
import com.kjw.twentyhour.listener.MyLocationListener;


public class StateActivity extends AppCompatActivity implements ChangePasswordDialog.Listener, CalendarFragment.onSetDateListener, FragmentDataSendInterface {


    private BottomNavigationView bottomNavigationView;
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;

    private String temp;
    private Time time;

//    int requestCode = 0;
//    private static final int TAKE_TO_SELECTED_LUGGAGE = 2;
//    private static final int TAKE_TO_CALENDARVIEW_RESULT = 1;
//    DownLoadTask downLoadTask;
//    private static long sayBackPress;

    public static final String TAG = StateActivity.class.getSimpleName();
    private static final int TAKE_TO_PRAYGRROUND_RESULT = 0;

    public View contact;
    public View home;
    public View more;
    public View search;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_state_setting);
//        -----------------------------------------------------------
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            return;
        }



//        ===========================================================


        ModelInit();
        LoadFragmentState();
        LoadDialogStoreSelection();
        initView();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.action_contact:

                        loadFragmentProfile();

                        a = true;
                        b = false;
                        c = false;
                        d = false;


                        if(a){
                            contact.setClickable(false);
                            home.setClickable(true);
                            search.setClickable(true);
                            more.setClickable(true);

                        } else {
                            contact.setClickable(true);
                        }

                        return true;

                    case R.id.action_home:

                        LoadFragmentState();


                        a = false;
                        b = true;
                        c = false;
                        d = false;

                        if(b){
                            contact.setClickable(true);
                            home.setClickable(false);
                            search.setClickable(true);
                            more.setClickable(true);

                        } else {
                            home.setClickable(true);
                        }


                        return true;

                    case R.id.action_search:

                        a = false;
                        b = false;
                        c = true;
                        d = false;

                        if(c){

                            contact.setClickable(true);
                            home.setClickable(true);
                            search.setClickable(false);
                            more.setClickable(true);

                        } else {
                            search.setClickable(true);
                        }

                        return true;



                    case R.id.action_more:

                        a = false;
                        b = false;
                        c = false;
                        d = true;

                        if(d){
                            contact.setClickable(true);
                            home.setClickable(true);
                            search.setClickable(true);
                            more.setClickable(false);
                        } else {
                            contact.setClickable(true);
                        }

                        return true;

                }
                return false;
            }
        });
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);



    }

    private void LoadDialogStoreSelection() {
        StoreSelectionDialog storeSelectionDialog = new StoreSelectionDialog();
        storeSelectionDialog.show(getSupportFragmentManager() , "aa");


    }

    private void ModelInit() {
        time = new Time();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == TAKE_TO_PRAYGRROUND_RESULT) {
                if (data.hasExtra("address")) {
                    temp = data.getStringExtra("address");
                    bundle = new Bundle();
                    bundle.putString("address", temp);

                    LoadFragmentState();
                }
            }
        }
    }


    private void LoadFragmentState() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        StateFragment stateFragment = new StateFragment();
        stateFragment.setArguments(bundle);
        ft.replace(R.id.fragmentFrame, stateFragment , StateFragment.TAG);
        ft.commit();
    }


    private void loadFragmentProfile() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        ft.replace(R.id.fragmentFrame,profileFragment,ProfileFragment.TAG);
        ft.commit();

    }


    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        bottomNavigationView.setSelectedItemId(R.id.action_home);


    }

//    private void setListener() {
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//                for(int i = 0; i< dotscount; i++){
//                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
//                }
//
//                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//    }

    private void initView() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);


        contact = (View) findViewById(R.id.action_contact);
        home    = (View) findViewById(R.id.action_home);
        search  = (View) findViewById(R.id.action_search);
        more    = (View) findViewById(R.id.action_more);





    }

//    private void setAdapter() {
//        AdViewPagerAdapter viewPagerAdapter = new AdViewPagerAdapter(this);
//        viewPager.setAdapter(viewPagerAdapter);
//        dotscount = viewPagerAdapter.getCount();
//        dotsCreate();
//    }

//    private void dotsCreate() {
//        dots = new ImageView[dotscount];
//
//        for(int i = 0; i < dotscount; i++){
//
//            dots[i] = new ImageView(this);
//            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            params.setMargins(8, 0 ,8, 0);
//
//            sliderDotspanel.addView(dots[i],params);
//        }
//
//        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//
//
//    }
//    @Override
//    public void onBackPressed() {
//        if (sayBackPress + 2000 > System.currentTimeMillis()){
//            super.onBackPressed();
//        }
//        else{
//            Toast.makeText(StateActivity.this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
//            sayBackPress = System.currentTimeMillis();
//        }
//    }

    @Override
    public void onPasswordChanged() {
        showSnackBarMessage("Password Changed Successfully !");

    }

    @Override
    public void setDate(String ss) {
        StateFragment sf = (StateFragment)getSupportFragmentManager().findFragmentByTag(StateFragment.TAG);
        sf.setDate(ss);

    }

    @Override
    public void transferTab() {

        StateFragment sf = (StateFragment)getSupportFragmentManager().findFragmentByTag(StateFragment.TAG);
        sf.transTab();

    }

    @Override
    public void myStartTimeStart(int selectedHour, int selectedMin) {

        time.setHour(selectedHour);
        time.setMin(selectedMin);

    }

    @Override
    public void myStartTimeEnd(int selectedHour, int selectedMin) {


        time.setHoure(selectedHour);
        time.setMine(selectedMin);
        StateFragment sf = (StateFragment)getSupportFragmentManager().findFragmentByTag(StateFragment.TAG);
        sf.setTime(time);


    }

}


