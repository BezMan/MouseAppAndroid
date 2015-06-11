package com.comrax.mouseappandroid.activities_N_fragments;


import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comrax.mouseappandroid.App;
import com.comrax.mouseappandroid.R;
import com.comrax.mouseappandroid.adapters.CustomAdapter;
import com.comrax.mouseappandroid.database.DBConstants;
import com.comrax.mouseappandroid.database.DBTools;
import com.comrax.mouseappandroid.model.ListModel;

import java.util.ArrayList;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;

public class SimpleStikkyFragment extends Fragment {

    private ListView mListView;
    private Button btn;
    TextView resultsTxtView;

    LocationManager mLocationManager;

    double lon, lat;

    CustomAdapter adapter;
    public ArrayList<ListModel> customListViewValuesArr = new ArrayList<>();

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public SimpleStikkyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simplelistview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultsTxtView = (TextView)getActivity().findViewById(R.id.txtResultsCount);

        mListView = (ListView) getActivity().findViewById(R.id.listview);
        btn = (Button) view.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "great", Toast.LENGTH_SHORT).show();
            }
        });
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,
                100, mLocationListener);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StikkyHeaderBuilder.stickTo(mListView)
                .setHeader(R.id.header, (ViewGroup) getView())
                .minHeightHeader(160)
                .build();


        populateListView();

        adapter = new CustomAdapter(getActivity(), customListViewValuesArr, getResources());
        mListView.setAdapter(adapter);


    }

    private void populateListView() {

        String cityId = App.getInstance().getCityId();
        String boneId = App.getInstance().getBoneId();
        Cursor cursor = new DBTools(getActivity()).getCurrentCityPlacesTable(DBConstants.PLACE_TABLE_NAME, DBConstants.cityId, cityId, DBConstants.boneId, boneId);

        resultsTxtView.setText("התקבלו "+ cursor.getCount() + " תוצאות");

            for (int i = 0; i < cursor.getCount(); cursor.moveToNext(), i++) {
                ListModel lm = new ListModel();

                lm.setTitleA(cursor.getString(cursor.getColumnIndex(DBConstants.name)));
                lm.setTitleB(cursor.getString(cursor.getColumnIndex(DBConstants.hebrewName)));
                lm.setTitleC(cursor.getString(cursor.getColumnIndex(DBConstants.type)));


                lm.setPrice(cursor.getInt(cursor.getColumnIndex(DBConstants.price)));
                lm.setAddress(cursor.getString(cursor.getColumnIndex(DBConstants.address)));

                lm.setRating(cursor.getFloat(cursor.getColumnIndex(DBConstants.rating)));

//                lm.setLatitude();
//                lm.setLongitude();


// The computed distance is stored in results[0].
//If results has length 2 or greater, the initial bearing is stored in results[1].
//If results has length 3 or greater, the final bearing is stored in results[2].

                float[] results = new float[1];

                Location.distanceBetween(lat, lon,
                        cursor.getDouble(cursor.getColumnIndex(DBConstants.centerCoordinateLat)), cursor.getDouble(cursor.getColumnIndex(DBConstants.centerCoordinateLon)), results);

                lm.setDistance(results[0]);
            //my lon and lat are 0//

                customListViewValuesArr.add(lm);

            }


    }

}
