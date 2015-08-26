package com.mouse.world.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mouse.world.R;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.model.CitiesModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by betzalel on 30/03/2015.
 */
public class CitiesAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private CitiesModel tempValues = null;
    /**
     * ******** Declare Used Variables ********
     */
    private CitiesAdapterInterface _activity;
    private ArrayList _listModelList;
    private int _existingCityCounter;
    private Resources _resources;


    /* **********  CustomAdapter Constructor ****************
    */
    public CitiesAdapter(CitiesAdapterInterface activity, ArrayList arrayList, int existingCityCounter, Resources resLocal) {

        /********** Take passed values **********/
        _activity = activity;
        _listModelList = arrayList;
        _existingCityCounter = existingCityCounter;
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

        public RelativeLayout backgroundLayout;
        public TextView nameCity;
        public ImageView imageCity, imageArrow;

    }

    /**
     * ******** Depends upon _arrayList size called for each row , Create each ListView row **********
     */
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        tempValues = null;
        tempValues = (CitiesModel) _listModelList.get(position);


        if (tempValues.getId().equals("greenBlankItem")) {
            view = inflater.inflate(R.layout.green_title, null);
        }
else if (tempValues.getId().equals("greenYesDownloaded")) {
                view = inflater.inflate(R.layout.green_title, null);

                TextView greenTv = (TextView)view.findViewById(R.id.greenTextView);
                greenTv.setText("מדריכי ערים שהורדתי");

            }

        else if (tempValues.getId().equals("greenNotDownloaded")) {
            view = inflater.inflate(R.layout.green_title, null);

            TextView greenTv = (TextView)view.findViewById(R.id.greenTextView);
            greenTv.setText("מדריכי ערים להורדה");

        }
        else if(tempValues.getId().equals("blankCityItem")){
            view = inflater.inflate(R.layout.grid_city_item_layout, null);
            holder.backgroundLayout = (RelativeLayout) view.findViewById(R.id.cityBackground);
            holder.backgroundLayout.setBackgroundColor(_resources.getColor(R.color.Achbar_gray_default_background));

        }

        else {


            view = inflater.inflate(R.layout.grid_city_item_layout, null);

            holder.nameCity = (TextView) view.findViewById(R.id.cityNameText);
            holder.imageCity = (ImageView) view.findViewById(R.id.cityImageView);
            holder.imageArrow = (ImageView) view.findViewById(R.id.cityImageArrow);

            /************  Set Model values in Holder elements ***********/
            holder.nameCity.setText(tempValues.getName());

            File file = new File(GlobalVars.getBasePath(((Activity) _activity).getApplicationContext(), "Default_master/" + tempValues.getImage()));
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                holder.imageCity.setImageBitmap(bitmap);
            }


            if (position < _existingCityCounter+2) {
                holder.imageArrow.setImageResource(_resources.getIdentifier("com.mouse.world:drawable/" + "side_arrow", null, null));
            } else {
                holder.imageArrow.setImageResource(_resources.getIdentifier("com.mouse.world:drawable/" + "down_arrow", null, null));

            }
            /******** Set Item Click Listner for LayoutInflater for each row ***********/
            view.setOnClickListener(new OnItemClickListener(position));

            view.setOnLongClickListener(new OnLongItemClickListener(position));


        }
        return view;
    }


    /**
     * ****** Called when Item click in ListView ***********
     */
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            _activity.onCityItemClick(mPosition);
        }
    }


    private class OnLongItemClickListener implements View.OnClickListener, View.OnLongClickListener {
        private int mPosition;

        OnLongItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            _activity.onLongCityItemClick(mPosition);
            return true;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface CitiesAdapterInterface{
        void onCityItemClick(int position);
        void onLongCityItemClick(int position);
    }


}
