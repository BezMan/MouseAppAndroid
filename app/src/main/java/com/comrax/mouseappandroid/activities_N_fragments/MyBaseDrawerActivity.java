package com.mouse.world.activities_N_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mouse.world.R;
import com.mouse.world.adapters.CustomGlobalNavDrawerAdapter;
import com.mouse.world.app.App;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.database.DBConstants;
import com.mouse.world.database.DBTools;
import com.mouse.world.model.DrawerModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public abstract class MyBaseDrawerActivity extends AppCompatActivity implements CustomGlobalNavDrawerAdapter.DrawerAdapterInterface{

    private AutoCompleteTextView itemDescriptionView;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    public ArrayList<DrawerModel> customDrawerItemsArr = new ArrayList<>();
    protected TextView appBarTextView;

    protected App myInstance = App.getInstance();
    protected DBTools dbTools = new DBTools(this);

    private static final String STATIC_PAGE_TAG = "staticPageTag";

    protected abstract int getLayoutResourceId();

    protected abstract String getTextForAppBar();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        setClickableIcons();

        mDrawerList = (ListView) findViewById(R.id.myNavList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mylist_drawer_layout);

        setNavDrawerData();



        itemDescriptionView = (AutoCompleteTextView) findViewById(R.id.autocomplete_desc);

        // Create an ItemAutoTextAdapter for the Item description field,
        // and set it as the OnItemClickListener for that field.
        ItemAutoTextAdapter adapter = this.new ItemAutoTextAdapter(dbTools);
        itemDescriptionView.setAdapter(adapter);
        itemDescriptionView.setOnItemClickListener(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        String str = getTextForAppBar();
        setupTextView(str);
    }

    protected void setupTextView(String text) {
        appBarTextView = (TextView) findViewById(R.id.title_text);
        appBarTextView.setText(text);
    }


    private void setClickableIcons() {
        ImageButton imageButtonInfo = (ImageButton) findViewById(R.id.image_info);
        imageButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStaticPage(4);

            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.image_burger);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCloseNavDrawer();
            }
        });
    }


    public void setNavDrawerData() {

        for (int i = 0; i < GlobalVars.Drawer_imageList.length; i++) {

            final DrawerModel item = new DrawerModel();

            /******* Firstly take data in model object ******/
            item.setBtnTitle(GlobalVars.Drawer_textList[i]);
            item.setBtnImage(GlobalVars.Drawer_imageList[i]);

            /******** Add Model Object in ArrayList **********/
            customDrawerItemsArr.add(item);
        }

        addDrawerItems();
        setupDrawer();
    }


    private void addDrawerItems() {
        CustomGlobalNavDrawerAdapter mAdapter = new CustomGlobalNavDrawerAdapter(this, customDrawerItemsArr, getResources());
        mDrawerList.setAdapter(mAdapter);

    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().getTextForAppBar("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().getTextForAppBar("regular");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void openCloseNavDrawer() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.END))
            mDrawerLayout.openDrawer(GravityCompat.START);

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();
    }

    @Override
    public void onNavDrawerItemClick(int mPosition) {

        if (mPosition == 0) {
            allCities();

        } else if (mPosition == 1) {
            openFavorites();

        } else if (mPosition == 2) {
            emailShareApp();

        } else if (mPosition == 3) {
            rateApp();

        } else {
            openStaticPage(mPosition);
        }
        mDrawerLayout.closeDrawers();
    }


    private void allCities() {
        Intent cityIntent = new Intent(this, MainGridActivity.class);
        cityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        cityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(cityIntent);
    }


    private void openFavorites() {
        Fragment staticPageFragment = getSupportFragmentManager().findFragmentByTag(STATIC_PAGE_TAG);

        //if frag open, just close it...
        if (this instanceof FavoritesActivity && staticPageFragment != null && staticPageFragment.isVisible()) { //if in static page:
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(staticPageFragment)
//                    .addToBackStack(fragment.getClass().getName())
                    .commit();

            setupTextView("המועדפים שלי");
        } else {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(intent);
        }

    }


    private void emailShareApp() {
        String websiteAddress = "http://www.mouse.co.il/";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "חבר/ה שלך הוריד/ה את אפליקציית עכבר עולם.");
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("הורד את האפליקציה באמצעות הלינק הבא: \n\n  <a href=\"" + websiteAddress + "\"> " + "להורדה" + "</a>"));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }



    private void rateApp() {
        String url = "http://www.mouse.co.il/appMouseWorld.aspx?action=rate&device=android";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }



    private void openStaticPage(int mPosition) {
        DrawerModel tempValues = customDrawerItemsArr.get(mPosition);
        for (int i = 0; i < GlobalVars.staticPagesArray.length(); i++) {
            try {

                JSONObject jsonObject = GlobalVars.staticPagesArray.getJSONObject(i);
                if ((mPosition == 4 && jsonObject.getString("id").equals("3")) || (mPosition == 5 && jsonObject.getString("id").equals("2")) || (mPosition == 6 && jsonObject.getString("id").equals("1"))) {


                    Bundle bundle = new Bundle();
                    bundle.putString("data", jsonObject.toString());
                    bundle.putString("barTitle", tempValues.getBtnTitle());

                    Fragment staticPageFragment = new StaticPageFragment();
                    staticPageFragment.setArguments(bundle);
                    loadFragment(staticPageFragment, STATIC_PAGE_TAG);

                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void loadFragment(final Fragment fragment, String fragTag) {

        Fragment staticPageFragment = getSupportFragmentManager().findFragmentByTag(STATIC_PAGE_TAG);

        if (staticPageFragment != null && staticPageFragment.isVisible()) { //if in static page:
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_container, fragment, fragTag)
//                    .addToBackStack(fragTag)
                    .commit();

        } else {   //not in static page :
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_container, fragment, fragTag)
                    .addToBackStack(fragTag)
                    .commit();

        }
    }



    @Override
    public void onBackPressed() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mylist_drawer_layout);
        Fragment staticPageFragment = getSupportFragmentManager().findFragmentByTag(STATIC_PAGE_TAG);

        //close drawer if opened:
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            if (staticPageFragment != null && staticPageFragment.isVisible()) { //if in static page:
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(staticPageFragment)
//                    .addToBackStack(fragment.getClass().getName())
                        .commit();

            }


            super.onBackPressed();

        }
    }


    class ItemAutoTextAdapter extends CursorAdapter implements android.widget.AdapterView.OnItemClickListener{

        /**
         * Constructor. Note that no cursor is needed when we create the
         * adapter. Instead, cursors are created on demand when completions are
         * needed for the field. (see
         * {@link ItemAutoTextAdapter#runQueryOnBackgroundThread(CharSequence)}.)
         *
         * @param dbHelper The AutoCompleteDbAdapter in use by the outer class
         *                 object.
         */
        public ItemAutoTextAdapter(DBTools dbHelper) {
            // Call the CursorAdapter constructor with a null Cursor.
            super(MyBaseDrawerActivity.this, null);
            dbTools = dbHelper;
        }

        /**
         * Invoked by the AutoCompleteTextView field to get completions for the
         * current input.
         * <p/>
         * NOTE: If this method either throws an exception or returns null, the
         * Filter class that invokes it will log an error with the traceback,
         * but otherwise ignore the problem. No choice list will be displayed.
         * Watch those error logs!
         *
         * @param constraint The input entered thus far. The resulting query will
         *                   search for Items whose description begins with this string.
         * @return A Cursor that is positioned to the first row (if one exists)
         * and managed by the activity.
         */
        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            if (getFilterQueryProvider() != null) {
                return getFilterQueryProvider().runQuery(constraint);
            }

            Cursor cursor = dbTools.fetchItemsByDesc((constraint != null ? constraint.toString() : "@@@@"));

            return cursor;
        }

        /**
         * Called by the AutoCompleteTextView field to get the text that will be
         * entered in the field after a choice has been made.
         *
         * @param cursor The cursor, positioned to a particular row in the list.
         * @return A String representing the row's text value. (Note that this
         * specializes the base class return value for this method,
         * which is {@link CharSequence}.)
         */
        @Override
        public String convertToString(Cursor cursor) {
            return itemDescriptionView.getText().toString();
//            final int columnIndex = cursor.getColumnIndexOrThrow(DBConstants.id);
//            final String str = cursor.getString(columnIndex);
//            return str;
        }

        /**
         * Called by the ListView for the AutoCompleteTextView field to display
         * the text for a particular choice in the list.
         *
         * @param view    The TextView used by the ListView to display a particular
         *                choice.
         * @param context The context (Activity) to which this form belongs;
         * @param cursor  The cursor for the list of choices, positioned to a
         *                particular row.
         */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView text1, text2;
            text1 = (TextView) view.findViewById(R.id.search_hebText);
            text2 = (TextView) view.findViewById(R.id.search_engText);

            if (App.getInstance().get_cityId()==null && cursor.getCount() == 1) {
                text1.setText("על מנת לבצע חיפוש");
                text2.setText("יש להוריד תחילה מדריך");

            } else {

                final int HebItemIndex = cursor.getColumnIndexOrThrow(DBConstants.hebrewName);
                final int EngItemIndex = cursor.getColumnIndexOrThrow(DBConstants.name);
                text1 = (TextView) view.findViewById(R.id.search_hebText);
                text1.setText(cursor.getString(HebItemIndex));
                text2.setText(cursor.getString(EngItemIndex));
            }
        }

        /**
         * Called by the AutoCompleteTextView field to display the text for a
         * particular choice in the list.
         *
         * @param context The context (Activity) to which this form belongs;
         * @param cursor  The cursor for the list of choices, positioned to a
         *                particular row.
         * @param parent  The ListView that contains the list of choices.
         * @return A new View (really, a TextView) to hold a particular choice.
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.item_list, parent, false);
            return view;
        }


        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
            //search item clicked:
            if (myInstance.get_cityId()==null) {//if we are in MainGrid Activity, not inside a city...
                return;
            }

            else {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                myInstance.set_boneId(cursor.getString(cursor.getColumnIndex(DBConstants.boneId)));
                myInstance.set_nsId(cursor.getString(cursor.getColumnIndex(DBConstants.nsId)));
                myInstance.set_objId(cursor.getString(cursor.getColumnIndex(DBConstants.objId)));

                Intent placeActivity = new Intent(MyBaseDrawerActivity.this, PlaceActivity.class);
                startActivity(placeActivity);

//                itemDescriptionView.setText("");
            }

        }
    }

}
