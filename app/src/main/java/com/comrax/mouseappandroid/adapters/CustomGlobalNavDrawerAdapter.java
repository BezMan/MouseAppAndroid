package com.mouse.world.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mouse.world.R;
import com.mouse.world.model.DrawerModel;

import java.util.ArrayList;


public class CustomGlobalNavDrawerAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private DrawerModel tempValues = null;
    private DrawerAdapterInterface _activity;
    private ArrayList _listModelList;
    private Resources _resources;



    /**
     * **********  CustomAdapter Constructor ****************
     */
    public CustomGlobalNavDrawerAdapter(DrawerAdapterInterface activity, ArrayList arrayList, Resources resLocal) {

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





    public View getView(final int position, View view, ViewGroup parent) {

        NavDrawerViewHolder holder = new NavDrawerViewHolder();
        tempValues = null;
        tempValues = (DrawerModel) _listModelList.get(position);

        view = inflater.inflate(R.layout.details_nav_drawer_item_layout, null);

        holder.textView = (TextView) view.findViewById(R.id.nav_drawer_text);
        holder.imageViewIcon = (ImageView) view.findViewById(R.id.nav_drawer_imageView);

        /************  Set Model values in Holder elements ***********/
        holder.textView.setText(tempValues.getBtnTitle());
        holder.imageViewIcon.setImageResource(_resources.getIdentifier("com.mouse.world:drawable/" + tempValues.getBtnImage(), null, null));

//        /******** Set Item Click Listner for LayoutInflater for each row ***********/
        if(position<7)
            view.setOnClickListener(new OnItemClickListener(position));
//
//
        return view;
    }



    /**
     * ****** Create a holder to contain inflated xml file elements **********
     */
    public static class NavDrawerViewHolder {

        public TextView textView;
        public ImageView imageViewIcon;

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
            _activity.onNavDrawerItemClick(mPosition);
        }
    }

    public interface DrawerAdapterInterface{
        void onNavDrawerItemClick(int position);
    }

}
