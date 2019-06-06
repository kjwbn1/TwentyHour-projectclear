package com.kjw.twentyhour;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kjw.twentyhour.data.Time;
import com.kjw.twentyhour.dialogs.StoreSelectionDialog;
import com.kjw.twentyhour.fragment.*;
import com.kjw.twentyhour.fragmenttofragmentinterface.FragmentDataSendInterface;
import com.kjw.twentyhour.helper.BottomNavigationViewHelper;
import com.kjw.twentyhour.listener.Subject;
import com.kjw.twentyhour.map.NMapPOIflagType;
import com.kjw.twentyhour.map.NMapViewerResourceProvider;
import com.kjw.twentyhour.model.Store;
import com.kjw.twentyhour.utils.Constants;
import com.kjw.twentyhour.utils.CurrentLoactionSearch;
import com.kjw.twentyhour.view.NmapActivity;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class StateActivity extends AppCompatActivity implements ChangePasswordDialog.Listener,
        CalendarFragment.onSetDateListener, FragmentDataSendInterface {


    public static final String TAG = StateActivity.class.getSimpleName();
    public static final int NOTIFICATION_ID = 888;
    private static boolean isContactClickable;
    private static boolean isHomeClickable;
    private static boolean isSearchClickable;
    private static boolean isMoreClickable;

    public MainFragment mainFragment;
    public ProfileFragment profileFragment;
    public Toolbar toolbar;

    private BottomNavigationView bottomNavigationView;
    public View contact;
    public View home;
    public View more;
    public View search;


    private Time time;

    public String shortestStore;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_setting);
        preferenceInit();
        initView();
        setSupportActionBar(toolbar);
        LoadMainFragment();


        time = new Time();


    }

    private void preferenceInit() {
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
    }

    private void initView() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        contact = (View) findViewById(R.id.action_contact);
        home = (View) findViewById(R.id.action_home);
        search = (View) findViewById(R.id.action_search);
        more = (View) findViewById(R.id.action_more);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.action_contact:

                    loadFragmentProfile();

                    isContactClickable = true;
                    isHomeClickable = false;
                    isSearchClickable = false;
                    isMoreClickable = false;

                    if (isContactClickable) {
                        contact.setClickable(false);
                        home.setClickable(true);
                        search.setClickable(true);
                        more.setClickable(true);
                    } else {
                        contact.setClickable(true);
                    }

                    return true;

                case R.id.action_home:


                    LoadMainFragment();
                    isContactClickable = false;
                    isHomeClickable = true;
                    isSearchClickable = false;
                    isMoreClickable = false;

                    if (isHomeClickable) {
                        contact.setClickable(true);
                        home.setClickable(false);
                        search.setClickable(true);
                        more.setClickable(true);
                    } else {
                        home.setClickable(true);
                    }

                    return true;

                case R.id.action_search:

                    LoadFragmentState();
                    isContactClickable = false;
                    isHomeClickable = false;
                    isSearchClickable = true;
                    isMoreClickable = false;

                    if (isSearchClickable) {

                        contact.setClickable(true);
                        home.setClickable(true);
                        search.setClickable(false);
                        more.setClickable(true);
                    } else {
                        search.setClickable(true);
                    }

                    return true;

                case R.id.action_more:

                    Intent intent = new Intent(this, StoreRegisterActivity.class);
                    startActivity(intent);


                    isContactClickable = false;
                    isHomeClickable = false;
                    isSearchClickable = false;
                    isMoreClickable = true;

                    if (isMoreClickable) {
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
        });


    }

    private void LoadMainFragment() {

        bundle = new Bundle();
        bundle.putString("shortestStore", shortestStore);

        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.fragmentFrame, mainFragment);
        ft.commit();
    }

    private void LoadFragmentState() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        StateFragment stateFragment = new StateFragment();
        stateFragment.setArguments(bundle);
        ft.replace(R.id.fragmentFrame, stateFragment, StateFragment.TAG);
        ft.commit();
    }

    private void loadFragmentProfile() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        profileFragment = new ProfileFragment();
        ft.replace(R.id.fragmentFrame, profileFragment, ProfileFragment.TAG);
        ft.commit();
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_profile), message, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public void setDate(String ss) {
        StateFragment sf = (StateFragment) getSupportFragmentManager().findFragmentByTag(StateFragment.TAG);
        sf.setDate(ss);

    }

    @Override
    public void onPasswordChanged() {
        showSnackBarMessage("Password Changed Successfully !");

    }

    @Override
    public void transferTab() {

        StateFragment sf = (StateFragment) getSupportFragmentManager().findFragmentByTag(StateFragment.TAG);
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
        StateFragment sf = (StateFragment) getSupportFragmentManager().findFragmentByTag(StateFragment.TAG);
        sf.setTime(time);


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
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        editor.putBoolean("selectedStore", false);
        editor.remove(Constants.TOKEN);
        editor.commit();

    }


}


