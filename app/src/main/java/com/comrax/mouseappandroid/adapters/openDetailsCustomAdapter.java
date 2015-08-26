package com.mouse.world.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mouse.world.app.App;
import com.mouse.world.R;
import com.mouse.world.activities_N_fragments.Open_Details_header_N_list;
import com.mouse.world.model.ListModel;

import java.util.ArrayList;

/**
 * Created by betzalel on 30/03/2015.
 */
public class OpenDetailsCustomAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private ListModel tempValues = null;
    /**
     * ******** Declare Used Variables ********
     */
    private OpenDetailsAdapterInterface _activity;
    private ArrayList _listModelList;
    private Resources _resources;

    /**
     * **********  CustomAdapter Constructor ****************
     */
    public OpenDetailsCustomAdapter(OpenDetailsAdapterInterface activity, ArrayList arrayList, Resources resLocal) {

        /********** Take passed values **********/
        _activity = activity;
        _listModelList = arrayList;
        _resources = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) ((Activity)_activity).getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {

        if (_listModelList.size() <= 0)
            return 1;
        return _listModelList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * ****** Create a holder to contain inflated xml file elements **********
     */
    public static class ViewHolder {

        public LinearLayout clickableLayout, wazeLayout;
        public TextView titleA, titleB, titleC, distance, address, textPrice;
        public ImageView imagePrice;
        public RatingBar ratingBar;
        public String objId, nsId;

    }

    /**
     * ******** Depends upon _arrayList size called for each row , Create each ListView row **********
     */
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        tempValues = null;
        tempValues = (ListModel) _listModelList.get(position);

        view = inflater.inflate(R.layout.open_details_list_item, null);

        holder.clickableLayout = (LinearLayout) view.findViewById(R.id.clickable_titles_layout);
        holder.wazeLayout = (LinearLayout) view.findViewById(R.id.waze_layout);

        holder.titleA = (TextView) view.findViewById(R.id.open_details_item_title_A);
        holder.titleB = (TextView) view.findViewById(R.id.open_details_item_title_B);
        holder.titleC = (TextView) view.findViewById(R.id.open_details_item_title_C);

        holder.distance = (TextView) view.findViewById(R.id.open_details_item_Distance);
        holder.address = (TextView) view.findViewById(R.id.open_details_item_address);
        holder.textPrice = (TextView) view.findViewById(R.id.open_details_item_price_text);
        holder.imagePrice = (ImageView) view.findViewById(R.id.open_details_item_price_image);
        holder.ratingBar = (RatingBar) view.findViewById(R.id.open_details_item_ratingBar);



        /************  Set Model values in Holder elements ***********/


        holder.objId = tempValues.getObjId();
        holder.nsId = tempValues.getNsId();

        holder.titleA.setText(tempValues.getTitleA());
        holder.titleB.setText(tempValues.getTitleB());
        holder.titleC.setText(tempValues.getTitleC());
        holder.address.setText(tempValues.getAddress());
        holder.ratingBar.setRating(tempValues.getRating() / 20);

        String coinNum = String.valueOf(tempValues.getPrice());
        holder.textPrice.setText(getHebrewTextPrice(coinNum));
        if(coinNum == "0") coinNum = "1";
        holder.imagePrice.setImageResource(_resources.getIdentifier("com.mouse.world:drawable/" + "coin_" + coinNum, null, null));


        holder.distance.setText(String.valueOf(tempValues.getDistance()));


        holder.clickableLayout.setOnClickListener(new OnPlaceItemClickListener(holder.objId, holder.nsId));

        holder.wazeLayout.setOnClickListener(new OnNavigateItemClickListener(holder.objId, holder.nsId));



        return view;
    }



    private String getHebrewTextPrice(String coinNum) {
        String hebString= "זול";
        switch (coinNum){
            case "2":
                hebString= "בינוני";
                break;
            case "3":
                hebString= "יקר";
                break;
        }
        return hebString;
    }





    private class OnPlaceItemClickListener implements View.OnClickListener {
        String mObjId, mNsId;

        OnPlaceItemClickListener(String objId, String nsId) {
            mObjId = objId;
            mNsId = nsId;
        }

        @Override
        public void onClick(View arg0) {
            App.getInstance().set_objId(mObjId);
            App.getInstance().set_nsId(mNsId);

            Open_Details_header_N_list myActivity = (Open_Details_header_N_list) _activity;
            myActivity.onPlaceItemClick();
        }
    }




    private class OnNavigateItemClickListener implements View.OnClickListener {
        String mObjId, mNsId;

        OnNavigateItemClickListener(String objId, String nsId) {
            mObjId = objId;
            mNsId = nsId;
        }

        @Override
        public void onClick(View v) {
            App.getInstance().set_objId(mObjId);
            App.getInstance().set_nsId(mNsId);

            Open_Details_header_N_list myActivity = (Open_Details_header_N_list) _activity;
            myActivity.onNavigationClick();

        }
    }

    public interface OpenDetailsAdapterInterface{
        void onNavigationClick();
        void onPlaceItemClick();
    }
}
