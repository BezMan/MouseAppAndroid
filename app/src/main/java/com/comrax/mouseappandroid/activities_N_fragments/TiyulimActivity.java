package com.mouse.world.activities_N_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.mouse.world.R;
import com.mouse.world.adapters.TiyulimListAdapter;
import com.mouse.world.database.DBConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bez on 14/07/2015.
 */
public class TiyulimActivity extends MyBaseDrawerActivity implements TiyulimListAdapter.TiyulimAdapterInterface{

    private ListView listView;
    private TiyulimListAdapter adapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tiulim_activity;
    }

    @Override
    protected String getTextForAppBar() {
        return myInstance.getCityName();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = (ListView)findViewById(R.id.tiyulim_listView);
        try {
            JSONObject jsonObject = new JSONObject(dbTools.getCellData(DBConstants.CITY_TABLE_NAME, DBConstants.touristArticlesList, DBConstants.cityId, myInstance.get_cityId()));
            JSONArray jsonArray = jsonObject.getJSONArray("articles");

            adapter = new TiyulimListAdapter(this, jsonArray);
            listView.setAdapter(adapter);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTiyulimItemClicked(JSONObject item) {
        Intent articleIntent = new Intent(this, ArticleActivity.class);
        articleIntent.putExtra("articleData", item.toString());
        startActivity(articleIntent);
    }


    }
