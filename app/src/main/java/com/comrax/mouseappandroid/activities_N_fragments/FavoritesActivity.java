package com.mouse.world.activities_N_fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mouse.world.R;
import com.mouse.world.app.App;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.database.DBConstants;
import com.mouse.world.helpers.AmazingAdapter;
import com.mouse.world.helpers.AmazingListView;
import com.mouse.world.model.FavoritesModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FavoritesActivity extends MyBaseDrawerActivity {
    private AmazingListView lsComposer;
    private FavoritesModel[][] allItems;
    private FavoritesAdapter adapter;
    private TextView editPage;
    private int currentHeader;

    private int mySize;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.favorites_activity_section_demo;
    }

    @Override
    protected String getTextForAppBar() {
        return "המועדפים שלי";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mySize = GlobalVars.detailMenuItems.size();
        allItems = new FavoritesModel[mySize][];

        lsComposer = (AmazingListView) findViewById(R.id.lsComposer);
        lsComposer.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.favorites_item_composer_header, lsComposer, false));
        adapter = new FavoritesAdapter(false);
        lsComposer.setAdapter(adapter);
    }


    class FavoritesAdapter extends AmazingAdapter {
        boolean mEditable;
        List<Pair<String, List<FavoritesModel>>> all;

        public FavoritesAdapter(boolean editable) {
            mEditable = editable;
            all = getAllData();
        }


        @Override
        public int getCount() {
            int res = 0;
            for (int i = 0; i < all.size(); i++) {
                res += all.get(i).second.size();
            }

            return res;

        }

        @Override
        public FavoritesModel getItem(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return all.get(i).second.get(position - c);
                }
                c += all.get(i).second.size();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        protected void onNextPageRequested(int page) {
        }

        @Override
        protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
            if (displaySectionHeader) {
                view.findViewById(R.id.header).setVisibility(View.VISIBLE);
                TextView lSectionTitle = (TextView) view.findViewById(R.id.header);
                lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
            } else {
                view.findViewById(R.id.header).setVisibility(View.GONE);
            }
        }


        //like getView of adapter//
        @Override
        public View getAmazingView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.favorites_item_composer, null);
            FavoritesModel favoritesModel = getItem(position);

            if (favoritesModel != null) {

                editPage = (TextView) findViewById(R.id.favorites_edit_page);

                TextView nameTxtView = (TextView) view.findViewById(R.id.favorite_name);
                TextView typeTxtView = (TextView) view.findViewById(R.id.favorite_type);

                LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.favorite_main_layout);

                if (mEditable) {
                    ImageView deleteBtn = (ImageView) view.findViewById(R.id.favorite_delete_image);
                    deleteBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setOnClickListener(new FavoritesLayoutClickListener(favoritesModel, position));

                } else {
                    currentHeader = getSectionForPosition(position);
                    mainLayout.setOnClickListener(new FavoritesLayoutClickListener(favoritesModel, currentHeader));
                }

                nameTxtView.setText(favoritesModel.getName());
                typeTxtView.setText(favoritesModel.getType());

                editPage.setOnClickListener(new FavoritesLayoutClickListener(favoritesModel, position));
            }


            return view;
        }

        @Override
        public void configurePinnedHeader(View header, int position, int alpha) {
            TextView lSectionHeader = (TextView) header;
            lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
//			lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
//			lSectionHeader.setTextColor(alpha << 24 | (0x222222));
        }

        @Override
        public int getPositionForSection(int section) {
            if (section < 0) section = 0;
            if (section >= all.size()) section = all.size() - 1;
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (section == i) {
                    return c;
                }
                c += all.get(i).second.size();
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return i;
                }
                c += all.get(i).second.size();
            }
            return -1;
        }

        @Override
        public String[] getSections() {
            String[] res = new String[all.size()];
            for (int i = 0; i < all.size(); i++) {
                res[i] = all.get(i).first;
            }
            return res;
        }

    }


    public List<Pair<String, List<FavoritesModel>>> getAllData() {
        List<Pair<String, List<FavoritesModel>>> pairList = new ArrayList<Pair<String, List<FavoritesModel>>>();

        for (int i = 0; i < 5; i++) {
            pairList.add(getOneSection(i));
        }
        return pairList;
    }


    public Pair<String, List<FavoritesModel>> getOneSection(int index) {
        String[] titles = new String[mySize];
        titles = GlobalVars.detailMenuItems.toArray(titles);

        if(myInstance.get_cityId()==null){
            return new Pair<String, List<FavoritesModel>>("", Arrays.<FavoritesModel>asList());
        }

        else {
            Cursor cursor = dbTools.getFavorites(DBConstants.FAVORITE_TABLE_NAME, DBConstants.cityId, myInstance.get_cityId(), DBConstants.boneCategoryName, titles[index]);

            allItems[index] = new FavoritesModel[cursor.getCount()];

            for (int j = 0; j < cursor.getCount(); j++, cursor.moveToNext()) {

                allItems[index][j] = new FavoritesModel(cursor);
            }
            return new Pair<String, List<FavoritesModel>>(titles[index], Arrays.asList(allItems[index]));
        }

    }


    private class FavoritesLayoutClickListener implements View.OnClickListener {
        FavoritesModel mFavoritesModel;
        int mCurrentHeaderPos;

        public FavoritesLayoutClickListener(FavoritesModel favoritesModel, int currentHeaderPos) {
            mFavoritesModel = favoritesModel;
            mCurrentHeaderPos = currentHeaderPos;
        }

        @Override
        public void onClick(View v) {

            if (v instanceof LinearLayout) {
                if (mCurrentHeaderPos > 0) { //place
                    Cursor cursor = dbTools.getData(DBConstants.FAVORITE_TABLE_NAME, DBConstants.name, mFavoritesModel.getName(), DBConstants.cityId, App.getInstance().get_cityId());

                    myInstance.set_boneIdTitle(cursor.getString(cursor.getColumnIndex(DBConstants.boneCategoryName)));
                    myInstance.set_boneId(cursor.getString(cursor.getColumnIndex(DBConstants.boneId)));
                    myInstance.set_nsId(cursor.getString(cursor.getColumnIndex(DBConstants.nsId)));
                    myInstance.set_objId(cursor.getString(cursor.getColumnIndex(DBConstants.objId)));

                    Intent placeActivity = new Intent(FavoritesActivity.this, PlaceActivity.class);
                    startActivity(placeActivity);
                }
                else { //article
                    Cursor cursor = dbTools.getData(DBConstants.FAVORITE_TABLE_NAME, DBConstants.name, mFavoritesModel.getName(), DBConstants.cityId, App.getInstance().get_cityId());

                    Intent articleActivity = new Intent(FavoritesActivity.this, ArticleActivity.class);
                    articleActivity.putExtra("articleData", cursor.getString(cursor.getColumnIndex(DBConstants.description)));
                    startActivity(articleActivity);

                }

            } else if (v instanceof ImageView) {//item delete btn:
                dbTools.deleteRow(DBConstants.FAVORITE_TABLE_NAME, DBConstants.name, mFavoritesModel.getName());

                adapter = new FavoritesAdapter(true);
                lsComposer.setAdapter(adapter);

            } else {// edit btn clicked:
                if (editPage.getText().equals("עריכה")) {//start editing//
                    editPage.setText("סיים");
                    adapter = new FavoritesAdapter(true);
                    lsComposer.setAdapter(adapter);


                } else {    //end editing//
                    editPage.setText("עריכה");
                    adapter = new FavoritesAdapter(false);
                    lsComposer.setAdapter(adapter);


                }

            }
        }
    }


}