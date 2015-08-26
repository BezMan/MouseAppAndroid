package com.mouse.world.activities_N_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mouse.world.R;
import com.mouse.world.adapters.MyPageAdapter;
import com.mouse.world.adapters.OpenDetailsCustomAdapter;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.app.HelperMethods;
import com.mouse.world.database.DBConstants;
import com.mouse.world.model.ListModel;
import com.mouse.world.model.MapMarkerModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viewpagerindicator.CirclePageIndicator;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;

/**
 * Created by bez on 07/06/2015.
 */
public class Open_Details_header_N_list extends MyBaseDrawerActivity implements OpenDetailsCustomAdapter.OpenDetailsAdapterInterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private TextView boneText;
    private Cursor cursor;
    private SlidingLayer mSlidingLayer;
    private GoogleMap map;
    private ArrayList<MapMarkerModel> markerArray;
    private List<Marker> markers = new ArrayList<>();
    private Marker currentMarker;
    private String prevIcon;
    private ListView mListView;
    private TextView resultsTxtView;

    private String realboneId;


    private OpenDetailsCustomAdapter adapter;
    private ArrayList<ListModel> customListViewValuesArr;

    private RadioGroup radioGroup;

    private ViewPager pager;
    private MyPageAdapter pageAdapter;

    private boolean setMap;

    protected static final String TAG = "location-updates-sample";

    public static final int GPS_SETTINGS = 9; //request code

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;


    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    public void mapButtonClicked(View v) {
        if (mSlidingLayer.isOpened()) {
            mSlidingLayer.closeLayer(true);
        } else {
            mSlidingLayer.openLayer(true);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();

        setBoneTitleAndColor();

        setupInitCityMap();

        setRadioGroupFilter();

        String data = myInstance.get_cityFolderName() + "/" + myInstance.get_cityId() + "_" + myInstance.get_boneId() + "_ArticalsList.json";
        JSONObject jsonData = HelperMethods.loadJsonDataFromFile(data);

        addPagerData(jsonData);

        setStikkyHeader();


        populateListView();

        adapter = new OpenDetailsCustomAdapter(this, customListViewValuesArr, getResources());
        mListView.setAdapter(adapter);

        toggleGPS();

        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices API.
        buildGoogleApiClient();


    }


    private void toggleGPS() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gpsStatus = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (myInstance.isToggleGPS() && !gpsStatus) { //gps is off:

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_toggle_gps_dialog);

            Button startGPSButton = (Button) dialog.findViewById(R.id.start_gps_btn);
            startGPSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    startGpsIntent();

                }
            });

            Button cancelGPSButton = (Button) dialog.findViewById(R.id.cancel_gps_btn);
            cancelGPSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myInstance.setToggleGPS(false);
                    dialog.dismiss();

                }
            });
            dialog.show();

        }
    }


        private void startGpsIntent(){
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(gpsIntent, GPS_SETTINGS);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == GPS_SETTINGS) {
            recreate();
        }
    }
            /**
             * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
             * LocationServices API.
             */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
//            setButtonsEnabledState();
            startLocationUpdates();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
//            setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//            startLocationUpdates();
            startUpdatesButtonHandler();
        }

        myInstance.set_boneId(realboneId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        cursor.close();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//            updateUI();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        stopUpdatesButtonHandler();

        int index = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());

        populateListView();

        adapter = new OpenDetailsCustomAdapter(this, customListViewValuesArr, getResources());
        mListView.setAdapter(adapter);

        mListView.setSelectionFromTop(index, top);

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    private void setStikkyHeader() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
//        int width = size.x;
//        int height = size.y;

//        Log.wtf("size: " , " width: " + size.x + " height: " + size.y);

        StikkyHeaderBuilder.stickTo(mListView)
                .setHeader(R.id.header, frameLayout)
//                .minHeightHeader(160)
                .minHeightHeader(size.y / 8)
                .build();
    }


    private void setRadioGroupFilter() {
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.btn1) {
                    screenListView(0);
                } else if (checkedId == R.id.btn2) {
                    screenListView(3);
                } else if (checkedId == R.id.btn3) {
                    screenListView(2);
                } else {
                    screenListView(1);
                }
            }

        });


    }


    private void initVars() {
        resultsTxtView = (TextView) findViewById(R.id.txtResultsCount);
        mListView = (ListView) findViewById(R.id.listview);
        pager = (ViewPager) findViewById(R.id.viewpager);

        realboneId = myInstance.get_boneId();
    }


    private void addPagerData(JSONObject jsonData) {
        List<Fragment> fragments = getFragmentsFromJson(jsonData);
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);

        pager.setAdapter(pageAdapter);

        //Bind the title indicator to the adapter
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.titles);
        indicator.setViewPager(pager);
    }


    private List<Fragment> getFragmentsFromJson(JSONObject jsonData) {
        List<Fragment> fList = new ArrayList<>();
        try {
            JSONArray articlesArray = jsonData.getJSONArray("articles");

            //lets add items thru loop
            for (int i = 0; i < 5; i++) {   //we want only 5 first urlContent = item.getJSONObject("urlContent").toString();tem.getString("image");
                JSONObject item = articlesArray.getJSONObject(i);
                String urlContent = item.getJSONObject("urlContent").toString();
                String image = item.getString("image").toString();

                fList.add(MyPagerArticleFragment.newInstance(urlContent, image));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fList;
    }


    private void screenListView(int price) {

        int index = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());

        if (price == 0) {

            adapter = new OpenDetailsCustomAdapter(this, customListViewValuesArr, getResources());
            mListView.setAdapter(adapter);

        } else {

            ArrayList<ListModel> screenedListViewValuesArr = new ArrayList<>();

            for (int i = 0; i < customListViewValuesArr.size(); i++) {
                if (customListViewValuesArr.get(i).getPrice() == price) {
                    screenedListViewValuesArr.add(customListViewValuesArr.get(i));
                }
            }
            adapter = new OpenDetailsCustomAdapter(this, screenedListViewValuesArr, getResources());
            mListView.setAdapter(adapter);
        }

        mListView.setSelectionFromTop(index, top);

    }


    private void populateListView() {

        customListViewValuesArr = new ArrayList<>();

        String cityId = myInstance.get_cityId();
        String boneId = myInstance.get_boneId();
        cursor = dbTools.getData(DBConstants.PLACE_TABLE_NAME, DBConstants.cityId, cityId, DBConstants.boneId, boneId);

        resultsTxtView.setText("התקבלו " + cursor.getCount() + " תוצאות");

        for (int i = 0; i < cursor.getCount(); cursor.moveToNext(), i++) {
            ListModel lm = new ListModel();

            lm.setTitleA(cursor.getString(cursor.getColumnIndex(DBConstants.name)));
            lm.setTitleB(cursor.getString(cursor.getColumnIndex(DBConstants.hebrewName)));
            lm.setTitleC(cursor.getString(cursor.getColumnIndex(DBConstants.type)));
            lm.setPrice(cursor.getInt(cursor.getColumnIndex(DBConstants.price)));
            lm.setAddress(cursor.getString(cursor.getColumnIndex(DBConstants.address)));
            lm.setRating(cursor.getFloat(cursor.getColumnIndex(DBConstants.rating)));
            lm.setObjId(cursor.getString(cursor.getColumnIndex(DBConstants.objId)));
            lm.setNsId(cursor.getString(cursor.getColumnIndex(DBConstants.nsId)));


            float[] results = new float[3];

            if (mCurrentLocation != null) {

                Location.distanceBetween(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
                        cursor.getDouble(cursor.getColumnIndex(DBConstants.centerCoordinateLat)), cursor.getDouble(cursor.getColumnIndex(DBConstants.centerCoordinateLon)), results);

                lm.setDistance(Math.round(results[0]) + " מטרים ממך ");

            }

            customListViewValuesArr.add(lm);

        }


    }


    private void setBoneTitleAndColor() {
        boneText = (TextView) findViewById(R.id.bone_title);
        String boneTitle = myInstance.get_boneIdTitle();
        boneText.setText(boneTitle);

        int pos = myInstance.getBonePosition();
        boneText.setBackgroundColor(GlobalVars.boneColors[pos - 1]);
    }


    private void setupInitCityMap() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Move the camera instantly to current city with a zoom of 10.
        String cityLat = dbTools.getCellData(DBConstants.CITY_TABLE_NAME, DBConstants.centerCoordinateLat, DBConstants.cityId, myInstance.get_cityId());
        String cityLon = dbTools.getCellData(DBConstants.CITY_TABLE_NAME, DBConstants.centerCoordinateLon, DBConstants.cityId, myInstance.get_cityId());

        //emulator vs device check//
        if (Build.BRAND.compareTo("generic") != 0) {
            LatLng zoomCamera = new LatLng(Double.parseDouble(cityLat), Double.parseDouble(cityLon));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomCamera, 12));
        }

        mSlidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer1);

    mSlidingLayer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
        @Override
        public void onOpen() {
        }

        @Override
        public void onShowPreview() {
        }

        @Override
        public void onClose() {
            mSlidingLayer.setSlidingEnabled(true);
        }

        @Override
        public void onOpened() {
            if (!setMap) {
                cursor = dbTools.getData(DBConstants.PLACE_TABLE_NAME, DBConstants.cityId, myInstance.get_cityId());
                setupMapData(cursor);
                setMap = true;
            }
            mSlidingLayer.setSlidingEnabled(false);
        }

        @Override
        public void onPreviewShowed() {
        }

        @Override
            public void onClosed() {
            }
        });
    }


    private void setupMapData(Cursor cursor) {
        markerArray = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); cursor.moveToNext(), i++) {
            final MapMarkerModel mapItem = new MapMarkerModel();
            mapItem.setBoneId(cursor.getString(cursor.getColumnIndex(DBConstants.boneId)));
            mapItem.setPlaceName(cursor.getString(cursor.getColumnIndex(DBConstants.name)));
            mapItem.setBoneCategoryId(cursor.getInt(cursor.getColumnIndex(DBConstants.boneCategoryId)));

            String itemLatitude = cursor.getString(cursor.getColumnIndex(DBConstants.centerCoordinateLat));
            String itemLongitude = cursor.getString(cursor.getColumnIndex(DBConstants.centerCoordinateLon));

            if (!itemLatitude.equals("")) {//latitude not empty val//

                // inside your loop:
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(itemLatitude), Double.parseDouble(itemLongitude)))
                        .title(mapItem.getPlaceName()));

                String currentIcon = GlobalVars.icon[mapItem.getBoneCategoryId()-1];

                marker.setIcon((BitmapDescriptorFactory
                        .fromResource(getResources().getIdentifier("com.mouse.world:drawable/" + "pin_" + currentIcon + "_blank", null, null))));

                markers.add(marker);
                markerArray.add(mapItem);
            }

        }


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                int currIndex = markers.indexOf(marker);
                int currBoneId = markerArray.get(currIndex).getBoneCategoryId();

                String currentIcon = GlobalVars.icon[currBoneId-1];

                if (currentMarker != null) {
                    currentMarker.setIcon(BitmapDescriptorFactory
                            .fromResource(getResources().getIdentifier("com.mouse.world:drawable/" + "pin_" + prevIcon + "_blank", null, null)));
                }
                currentMarker = marker;
                prevIcon = currentIcon;
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(getResources().getIdentifier("com.mouse.world:drawable/" + "pin_" + currentIcon, null, null)));

                return false;
            }
        });


        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // Getting view from the layout file map_info_window
                View v = getLayoutInflater().inflate(R.layout.map_info_window, null);

                // Setting the title
                TextView infoWindow = (TextView) v.findViewById(R.id.tv_place_name);
                infoWindow.setText(marker.getTitle() + "   ");

                // Returning the view containing InfoWindow contents
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Cursor cursor = dbTools.getData(DBConstants.PLACE_TABLE_NAME, DBConstants.name, marker.getTitle(), DBConstants.cityId, myInstance.get_cityId());

                myInstance.set_boneId(cursor.getString(cursor.getColumnIndex(DBConstants.boneId)));
                myInstance.set_nsId(cursor.getString(cursor.getColumnIndex(DBConstants.nsId)));
                myInstance.set_objId(cursor.getString(cursor.getColumnIndex(DBConstants.objId)));

                startActivity(new Intent(getApplicationContext(), PlaceActivity.class));

                closeSlidingMapPanel();
            }
        });
    }

    private void closeSlidingMapPanel() {
        if (mSlidingLayer.isOpened()) {
            mSlidingLayer.closeLayer(true);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mSlidingLayer.isOpened()) {
                    mSlidingLayer.closeLayer(true);
                    return true;
                }

            default:
                return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.open_details_full_header;

    }

    @Override
    protected String getTextForAppBar() {
        return myInstance.getCityName();
    }


    @Override
    public void onPlaceItemClick() {
        startActivity(new Intent(getApplicationContext(), PlaceActivity.class));
    }


    @Override
    public void onNavigationClick() {

        cursor = dbTools.getData(DBConstants.PLACE_TABLE_NAME, DBConstants.cityId, myInstance.get_cityId(), DBConstants.boneId, myInstance.get_boneId(), DBConstants.objId, myInstance.get_objId());

        String latitude = cursor.getString(cursor.getColumnIndex(DBConstants.centerCoordinateLat));
        String longitude = cursor.getString(cursor.getColumnIndex(DBConstants.centerCoordinateLon));

        try {
            String url = "geo:" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }

    }




}




