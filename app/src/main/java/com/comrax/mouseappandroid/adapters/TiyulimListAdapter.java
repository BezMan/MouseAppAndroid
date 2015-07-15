package com.comrax.mouseappandroid.adapters;

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
import android.widget.TextView;

import com.comrax.mouseappandroid.R;
import com.comrax.mouseappandroid.activities_N_fragments.TiyulimActivity;
import com.comrax.mouseappandroid.app.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class TiyulimListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    /**
     * ******** Declare Used Variables ********
     */
    private Activity _activity;
    private JSONArray _jsonArray;
    private Resources _resources;

    /**
     * **********  CustomAdapter Constructor ****************
     */
    public TiyulimListAdapter(Activity activity, JSONArray jsonArray, Resources resLocal) {

        /********** Take passed values **********/
        _activity = activity;
        _jsonArray = jsonArray;
        _resources = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {

        if (_jsonArray.length() <= 0)
            return 1;
        return _jsonArray.length();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View view, ViewGroup parent) {

        TiyulimListViewHolder holder = new TiyulimListViewHolder();
        try {
            JSONObject item = _jsonArray.getJSONObject(position);


            view = inflater.inflate(R.layout.tiyulim_item_layout, null);

            holder.title = (TextView) view.findViewById(R.id.tiyulim_title);
            holder.text = (TextView) view.findViewById(R.id.tiyulim_main_text);
            holder.imageView = (ImageView) view.findViewById(R.id.tiyulim_imageView);

            /************  Set Model values in Holder elements ***********/

            holder.title.setText(item.getString("nameTitle"));
            JSONObject urlContent = item.getJSONObject("urlContent");

            holder.text.setText(urlContent.getString("description"));

            File file = new File(App.getInstance().get_cityFolderName() + "/" + item.getString("image"));
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                holder.imageView.setImageBitmap(bitmap);
            }

            view.setOnClickListener(new OnTiyulimListItemClicked(position));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;

    }


    public static class TiyulimListViewHolder {

        public TextView title, text;
        public ImageView imageView;

    }


    private class OnTiyulimListItemClicked implements View.OnClickListener {
        private int mPosition;

        OnTiyulimListItemClicked(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            try {
                JSONObject item = _jsonArray.getJSONObject(mPosition);

                TiyulimActivity myActivity = (TiyulimActivity) _activity;
                myActivity.onTiyulimItemClicked(item);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
