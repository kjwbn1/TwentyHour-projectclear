package com.kjw.twentyhour;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.kjw.twentyhour.fragment.LoginFragment;
import com.kjw.twentyhour.fragment.LoginFragmentOwner;
import com.kjw.twentyhour.fragment.ResetPasswordDialog;
import com.kjw.twentyhour.map.NMapPOIflagType;
import com.kjw.twentyhour.map.NMapViewerResourceProvider;
import com.kjw.twentyhour.utils.Constants;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;

import java.util.*;


public class LoginActivity extends AppCompatActivity implements ResetPasswordDialog.Listener {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 33;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";

    private LoginFragment mLoginFragment;
    private LoginFragmentOwner mLoginFragmentOwner;

    private HashMap<String, Double> Stores;

    private String shortestStore;
    private String key = "";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private LocationCallback mLocationCallback;
    private Bundle bundle;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean mRequestingLocationUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (savedInstanceState == null) {

            loadFragment();
        }

        bundle = new Bundle();

        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> { });
        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(LoginActivity.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    shortestDistenceCalculation(location.getLatitude(),location.getLongitude());
                }
            }

            ;
        };

        updateValuesFromBundle(savedInstanceState);

    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            mRequestingLocationUpdates = savedInstanceState.getBoolean(
                    REQUESTING_LOCATION_UPDATES_KEY);
        }
        //        updateUI();
    }

    private void loadFragment() {

        if (mLoginFragment == null) {

            boolean isSelectedC = getIntent().getBooleanExtra("isSelectedC", true);

            if (isSelectedC) {

//                mLoginFragment.setArguments(bundle);
                mLoginFragment = new LoginFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame, mLoginFragment, LoginFragment.TAG).commit();

            } else {
                mLoginFragmentOwner = new LoginFragmentOwner();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame, mLoginFragmentOwner).commit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }


    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLastLocation = task.getResult();
                        shortestDistenceCalculation(mLastLocation.getLatitude(), mLastLocation.getLongitude());

//                            mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
//                                    mLatitudeLabel,
//                                    mLastLocation.getLatitude()));
//                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
//                                    mLongitudeLabel,
//                                    mLastLocation.getLongitude()));
                    } else {
                        Log.w(TAG, "getLastLocation:exception", task.getException());
//                            showSnackbar("위치가 감지되지 않음.");
                    }
                });
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);

        super.onSaveInstanceState(outState);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }


    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopLocationUpdates();

    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


//        String data = intent.getStringExtra(Constants.TOKEN);
//        Log.d(TAG, "onNewIntent: "+data);
//
//        mResetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);
//
//        if (mResetPasswordDialog != null)
//            mResetPasswordDialog.setToken(data);
    }

    @Override
    public void onPasswordReset(String message) {

        showSnackBarMessage(message);

    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.main_activity_container), message, Snackbar.LENGTH_SHORT).show();

    }


    private void shortestDistenceCalculation(Double latitude, Double longitude) {
        NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(10, mMapViewerResourceProvider);
        poiData.beginPOIdata(0);
        NMapPOIitem item = poiData.addPOIitem(126.9522394, 37.5143606, "봉은사역점", markerId, 0);
        poiData.addPOIitem(126.924409, 37.498442, "보라메공원", markerId, 1);
        poiData.addPOIitem(126.9522394, 37.464007, "서울대점", markerId, 2);
        poiData.addPOIitem(126.930166, 37.484454, "르네상스", markerId, 3);
        poiData.addPOIitem(126.930080, 37.483844, "포도몰점", markerId, 4);
        poiData.addPOIitem(126.929329, 37.483874, "롯데쇼핑점", markerId, 5);
        poiData.addPOIitem(126.926935, 37.487258, "스포렉스", markerId, 6);

        item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
        poiData.endPOIdata();


        NGeoPoint from = new NGeoPoint();
        from.set(latitude, longitude);


        NGeoPoint distance = new NGeoPoint();
        ArrayList<Double> disArray = new ArrayList<>();
        Double dis = null;
        Stores = new HashMap<>();

        for (int i = 0; i < poiData.count(); i++) {

            NGeoPoint to = new NGeoPoint();
            to.set(poiData.getPOIitem(i).getPoint().getLongitude(), poiData.getPOIitem(i).getPoint().getLatitude());
            poiData.getPOIitem(i).getTitle();


            dis = distance.getDistance(from, to);
            Stores.put(poiData.getPOIitem(i).getTitle(), dis);
            disArray.add(dis);
        }

        Double min = Collections.min(Stores.values());


        for (Map.Entry<String, Double> entry : Stores.entrySet()) {
            if (entry.getValue() == min) {
                key = entry.getKey();
            }

        }


        System.out.println(Stores.get(key).toString());

        shortestStore = key;

        editor.putString("shortestStore", shortestStore);
        editor.commit();

    }


}