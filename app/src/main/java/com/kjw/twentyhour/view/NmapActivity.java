package com.kjw.twentyhour.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.data.Okjson;
import com.kjw.twentyhour.data.Time;
import com.kjw.twentyhour.factory.StoreFactory;
import com.kjw.twentyhour.listener.StoreBranch;
import com.kjw.twentyhour.listener.StoreData;
import com.kjw.twentyhour.map.NMapCalloutCustomOldOverlay;
import com.kjw.twentyhour.map.NMapPOIflagType;
import com.kjw.twentyhour.map.NMapViewerResourceProvider;
import com.kjw.twentyhour.model.Staff;
import com.kjw.twentyhour.network.MySingleton;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Sample class for map viewer library.
 *
 * @author kyjkim
 */
public class NmapActivity extends NMapActivity  {

    private static final String LOG_TAG = "NmapActivity";
    private static final boolean DEBUG = false;
    private static final String NAVERMAPURL = "https://openapi.naver.com/v1/map/reversegeocode?"+" encoding=utf-8&coordType=latlng&query=";
    // set your Client ID which is registered for NMapViewer library.
    private static final String CLIENT_ID = "pJ4dLhVpOHDtq762zrhh";

    private MapContainerView mMapContainerView;
    SharedPreferences mSharedPreferences;

    private NMapView mMapView;
    private NMapController mMapController;

    private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(126.978371, 37.5666091);
    private static final int NMAP_ZOOMLEVEL_DEFAULT = 11;
    private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;
    private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false;
    private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false;

    private static final String KEY_ZOOM_LEVEL = "NmapActivity.zoomLevel";
    private static final String KEY_CENTER_LONGITUDE = "NmapActivity.centerLongitudeE6";
    private static final String KEY_CENTER_LATITUDE = "NmapActivity.centerLatitudeE6";
    private static final String KEY_VIEW_MODE = "NmapActivity.viewMode";
    private static final String KEY_TRAFFIC_MODE = "NmapActivity.trafficMode";
    private static final String KEY_BICYCLE_MODE = "NmapActivity.bicycleMode";

    private SharedPreferences mPreferences;

    private NMapOverlayManager mOverlayManager;

    private NMapMyLocationOverlay mMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;

    private NMapViewerResourceProvider mMapViewerResourceProvider;

    private NMapPOIdataOverlay mFloatingPOIdataOverlay;
    private NMapPOIdataOverlay mNMapPOIdataOverlay;
    private NMapPOIitem mFloatingPOIitem;
    private NMapPOIitem mFixPOIitem;
    private NMapPOIdata poidata;
    ArrayList<StoreBranch> ss;

    SharedPreferences.Editor edit;

    private String clientId = "pJ4dLhVpOHDtq762zrhh";
    private String clientSecret = "RAy133RV9k";
    int markerId;

    static Double x;
    static Double y;
    StoreFactory storeFactory;

    private static boolean USE_XML_LAYOUT = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (USE_XML_LAYOUT) {
            setContentView(R.layout.activity_naver_map);
            mMapView = (NMapView)findViewById(R.id.mapView);
        } else {
            mMapView = new NMapView(this);
            mMapContainerView = new MapContainerView(this);
            mMapContainerView.addView(mMapView);
            setContentView(mMapContainerView);
        }
        initSharedPreferences();

        mMapView.setClientId(CLIENT_ID);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);

        mMapController = mMapView.getMapController();

        NMapView.LayoutParams lp = new NMapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
        mMapView.setBuiltInZoomControls(true, lp);

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        super.setMapDataProviderListener(onDataProviderListener);

        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
        // register callout overlay view listener to customize it.
//        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);

//        // location manager
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        mMapCompassManager = new NMapCompassManager(this);
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

        mOverlayManager.clearOverlays();

        markerId = NMapPOIflagType.PIN;


        ObjectMapper mapper = new ObjectMapper();
        StoreData sd = new StoreData();
        StoreBranch sb = new StoreBranch(sd, "");
        Time time = new Time();
        Okjson okjson = new Okjson();
        Staff staff = new Staff();



        startMyLocation();
        startOverLayout();

        }

    public void startOverLayout() {
        NMapPOIdata poiData = new NMapPOIdata(10, mMapViewerResourceProvider);
        poiData.beginPOIdata(0);
        NMapPOIitem item = poiData.addPOIitem(127.0630205, 37.5091300, "", markerId, 0);
        poiData.addPOIitem(126.9190839,37.4932598, "", markerId, 1);
        poiData.addPOIitem(126.9587816,37.4438063, "", markerId, 2);
        poiData.addPOIitem(126.930166,37.484454,"",markerId,3);
//        르네상스
        poiData.addPOIitem(126.930080,37.483844,"",markerId,4);
//        포토몰
        poiData.addPOIitem(126.929329,37.483874,"",markerId,5);
//        롯데리아
        poiData.addPOIitem(126.926935,37.487258,"",markerId,6);
//        보라메공원
        item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
        poiData.endPOIdata();

        StoreData storeData = new StoreData();
        storeFactory = new StoreFactory();




        for(int i =0; i < poiData.count(); i++ ) {

            NMapPOIitem Items = poiData.getPOIitem(i);
            NGeoPoint point = Items.getPoint();
            x = point.latitude;
            y = point.longitude;
            String url = NAVERMAPURL + y + "," + x ;

            CalculatorcloseDistence(x , y);

            storeFactory.createStoreBranch(i,storeData);
            StoreBranch storeBranch = storeFactory.getStoreFranches().get(i);

            StringRequest stringRequest = new StringRequest(Request.Method.GET,  url,
                    response -> {

                        Gson gson = new Gson();

                        Okjson asshole = gson.fromJson(response , Okjson.class);

                        String address = asshole.getResult().getItems().get(1).address;
                        Double longitude = asshole.getResult().getItems().get(1).getPoint().getX();
                        Double latitude = asshole.getResult().getItems().get(1).getPoint().getY();
                        Items.setTitle(address);
                        storeBranch.setAddress(address);
                        storeBranch.setLongitude(longitude);
                        storeBranch.setLatitude(latitude);


                        },

                    error -> { })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Naver-Client-Id" , clientId);
                    headers.put("X-Naver-Client-Secret", clientSecret);
                    return headers;
                }
            };



            MySingleton.getInstance(this).addToRequestQueue(stringRequest);

        }
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
        poiDataOverlay.selectPOIitem(0, true);
        mNMapPOIdataOverlay = poiDataOverlay;


    }

    private void CalculatorcloseDistence(Double longitude, Double latitude) {




        NGeoPoint nGeoPointFrom = new NGeoPoint();
        nGeoPointFrom.set(longitude, latitude);

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
    protected void onStop() {
        super.onStop();
        stopMyLocation();
        storeFactory.getStoreFranches().get(1);

    }

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        edit = mSharedPreferences.edit();

    }

    @Override
    protected void onDestroy() {

        // save map view state such as map center position and zoom level.
        saveInstanceState();

        super.onDestroy();
    }

    /* Test Functions */

    private void startMyLocation() {

        if (mMyLocationOverlay != null) {
            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
                mOverlayManager.addOverlay(mMyLocationOverlay);
            }

            if (mMapLocationManager.isMyLocationEnabled()) {

                if (!mMapView.isAutoRotateEnabled()) {
                    mMyLocationOverlay.setCompassHeadingVisible(true);

                    mMapCompassManager.enableCompass();

                    mMapView.setAutoRotateEnabled(true, false);

                    mMapContainerView.requestLayout();
                } else {
                    stopMyLocation();
                }

                mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(NmapActivity.this, "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goToSettings);

                    return;
                }
            }
        }
    }

    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

                mMapContainerView.requestLayout();
            }
        }
    }

    /* NMapDataProvider Listener */
    private final OnDataProviderListener onDataProviderListener = new OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            if (DEBUG) {
                Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
                        + ((placeMark != null) ? placeMark.toString() : null));
            }

            if (errInfo != null) {
                Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                Toast.makeText(NmapActivity.this, errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (mFixPOIitem != null && mNMapPOIdataOverlay != null) {
                mNMapPOIdataOverlay.deselectFocusedPOIitem();

                if (placeMark != null) {
                    mFixPOIitem.setTitle(placeMark.toString());
                }
                mNMapPOIdataOverlay.selectPOIitemBy(mFixPOIitem.getId(), false);
            }
        }

    };

    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

            // stop location updating
            //			Runnable runnable = new Runnable() {
            //				public void run() {
            //					stopMyLocation();
            //				}
            //			};
            //			runnable.run();

            Toast.makeText(NmapActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(NmapActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

            stopMyLocation();
        }

    };

    /* MapView State Change Listener*/
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.
                restoreInstanceState();

            } else { // fail
                Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());

                Toast.makeText(NmapActivity.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {

        }
    };

    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLongPressCanceled(NMapView mapView) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {

        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
        }

        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub


        }

    };

    private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {

        @Override
        public boolean isLocationTracking() {
            if (mMapLocationManager != null) {
                if (mMapLocationManager.isMyLocationEnabled()) {
                    return mMapLocationManager.isMyLocationFixed();
                }
            }
            return false;
        }

    };

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());


            }


            Intent data = new Intent();
            data.putExtra("address" , item.getTitle());
            setResult( RESULT_OK , data);
            finish();


//            Intent data = new Intent();
//            data.putExtra("myData2", item.getTitle());
//            setResult(RESULT_OK, data);
//            finish();

            // [[TEMP]] handle a click event of the callout
            Toast.makeText(NmapActivity.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                if (item != null) {
                    Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.i(LOG_TAG, "onFocusChanged: ");
                }
            }
        }
    };

    private final NMapPOIdataOverlay.OnFloatingItemChangeListener onPOIdataFloatingItemChangeListener = new NMapPOIdataOverlay.OnFloatingItemChangeListener() {

        @Override
        public void onPointChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            NGeoPoint point = item.getPoint();

            if (DEBUG) {
                Log.i(LOG_TAG, "onPointChanged: point=" + point.toString());
            }

            findPlacemarkAtLocation(point.longitude, point.latitude);

            item.setTitle(null);

        }
    };

    private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
                                                         Rect itemBounds) {

            // handle overlapped items
            if (itemOverlay instanceof NMapPOIdataOverlay) {
                NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

                // check if it is selected by touch event
                if (!poiDataOverlay.isFocusedBySelectItem()) {
                    int countOfOverlappedItems = 1;

                    NMapPOIdata poiData = poiDataOverlay.getPOIdata();
                    for (int i = 0; i < poiData.count(); i++) {
                        NMapPOIitem poiItem = poiData.getPOIitem(i);

                        // skip selected item
                        if (poiItem == overlayItem) {
                            continue;
                        }

                        // check if overlapped or not
                        if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                            countOfOverlappedItems++;
                        }
                    }

                    if (countOfOverlappedItems > 1) {
                        String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
                        Toast.makeText(NmapActivity.this, text, Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
            }

            // use custom old callout overlay
            if (overlayItem instanceof NMapPOIitem) {
                NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

                if (poiItem.showRightButton()) {
                    return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mMapViewerResourceProvider);
                }
            }

            // use custom callout overlay
            return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

            // set basic callout overlay
            //return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
        }

    };

//    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {
//
//        @Override
//        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
//
//            if (overlayItem != null) {
//                // [TEST] 말풍선 오버레이를 뷰로 설정함
//                String title = overlayItem.getTitle();
//                if (title != null && title.length() > 5) {
//                    return new NMapCalloutCustomOverlayView(NmapActivity.this, itemOverlay, overlayItem, itemBounds);
//                }
//            }
//
//            // null을 반환하면 말풍선 오버레이를 표시하지 않음
//            return null;
//        }
//
//    };

    /* Local Functions */
    private static boolean mIsMapEnlared = false;

    private void restoreInstanceState() {
        mPreferences = getPreferences(MODE_PRIVATE);

        int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
        int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
        int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
        int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
        boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE, NMAP_TRAFFIC_MODE_DEFAULT);
        boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE, NMAP_BICYCLE_MODE_DEFAULT);

        mMapController.setMapViewMode(viewMode);
        mMapController.setMapViewTrafficMode(trafficMode);
        mMapController.setMapViewBicycleMode(bicycleMode);
        mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);

        if (mIsMapEnlared) {
            mMapView.setScalingFactor(2.0F);
        } else {
            mMapView.setScalingFactor(1.0F);
        }
    }

    private void saveInstanceState() {
        if (mPreferences == null) {
            return;
        }

        NGeoPoint center = mMapController.getMapCenter();
        int level = mMapController.getZoomLevel();
        int viewMode = mMapController.getMapViewMode();
        boolean trafficMode = mMapController.getMapViewTrafficMode();
        boolean bicycleMode = mMapController.getMapViewBicycleMode();

        SharedPreferences.Editor edit = mPreferences.edit();

        edit.putInt(KEY_CENTER_LONGITUDE, center.getLongitudeE6());
        edit.putInt(KEY_CENTER_LATITUDE, center.getLatitudeE6());
        edit.putInt(KEY_ZOOM_LEVEL, level);
        edit.putInt(KEY_VIEW_MODE, viewMode);
        edit.putBoolean(KEY_TRAFFIC_MODE, trafficMode);
        edit.putBoolean(KEY_BICYCLE_MODE, bicycleMode);


        edit.commit();

    }

    private static final long AUTO_ROTATE_INTERVAL = 2000;
    private final Handler mHnadler = new Handler();
    private final Runnable mTestAutoRotation = new Runnable() {
        @Override
        public void run() {
//        	if (mMapView.isAutoRotateEnabled()) {
//    			float degree = (float)Math.random()*360;
//
//    			degree = mMapView.getRoateAngle() + 30;
//
//    			mMapView.setRotateAngle(degree);
//
//            	mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);
//        	}
        }
    };

    /**
     * Container view class to rotate map view.
     */
    private class MapContainerView extends ViewGroup {

        public MapContainerView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            final int width = getWidth();
            final int height = getHeight();
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);
                final int childWidth = view.getMeasuredWidth();
                final int childHeight = view.getMeasuredHeight();
                final int childLeft = (width - childWidth) / 2;
                final int childTop = (height - childHeight) / 2;
                view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            }

            if (changed) {
                mOverlayManager.onSizeChanged(width, height);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            int sizeSpecWidth = widthMeasureSpec;
            int sizeSpecHeight = heightMeasureSpec;

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);

                if (view instanceof NMapView) {
                    if (mMapView.isAutoRotateEnabled()) {
                        int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
                        sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
                        sizeSpecHeight = sizeSpecWidth;
                    }
                }

                view.measure(sizeSpecWidth, sizeSpecHeight);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
