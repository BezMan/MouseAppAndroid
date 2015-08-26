package com.mouse.world.activities_N_fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mouse.world.R;
import com.mouse.world.adapters.CitiesAdapter;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.app.HelperMethods;
import com.mouse.world.database.DBConstants;
import com.mouse.world.model.BannersModel;
import com.mouse.world.model.CitiesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by bez on 10/05/2015.
 */
public class MainGridActivity extends MyBaseDrawerActivity implements CitiesAdapter.CitiesAdapterInterface {

    private GridViewWithHeaderAndFooter gridView;
    private ArrayList<CitiesModel> CitiesArray;
    private CitiesAdapter citiesAdapter;

    private View bannerLayout;
    public Button b1, b2, b3, b4;
    public ImageView image1, image2, image3, image4;
    public LinearLayout layout1, layout2, layout3, layout4;

    private int existingCityCounter;

    private String fileName, updateDate;
    private File sourceZipFile, destinationFolder;

    private ArrayList<Pair<String, String>> boneId_boneName;

    private CitiesModel updatedCity;
    private boolean isUpdate;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.my_drawer_layout;
    }

    @Override
    protected String getTextForAppBar() {
        return "עכבר עולם";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVarsAndHeaders();

        saveStaticPages(HelperMethods.loadJsonDataFromFile(GlobalVars.getBasePath(getApplicationContext(), "Default_master/staticPages.json")));
        setBanners(HelperMethods.loadJsonDataFromFile(GlobalVars.getBasePath(getApplicationContext(), "Default_master/banners.json")));
        setCities(HelperMethods.loadJsonDataFromFile(GlobalVars.getBasePath(getApplicationContext(), "Default_master/cities.json")));

        addDummyViews();

        citiesAdapter = new CitiesAdapter(this, CitiesArray, existingCityCounter, getResources());
        gridView.setAdapter(citiesAdapter);

    }


    private void saveStaticPages(JSONObject jsonObject) {
        try {
            GlobalVars.staticPagesArray = jsonObject.getJSONArray("pages");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void addDummyViews() {

        final CitiesModel greenNotDownloaded = new CitiesModel();
        greenNotDownloaded.setId("greenNotDownloaded");

        final CitiesModel greenYesDownloaded = new CitiesModel();
        greenYesDownloaded.setId("greenYesDownloaded");

        final CitiesModel greenBlankItem = new CitiesModel();
        greenBlankItem.setId("greenBlankItem");

        final CitiesModel blankCityItem = new CitiesModel();
        blankCityItem.setId("blankCityItem");


        if (existingCityCounter > 0) {

            //2 greens at middle:
            CitiesArray.add(existingCityCounter, greenBlankItem);
            CitiesArray.add(existingCityCounter, greenNotDownloaded);
            if (existingCityCounter % 2 == 1) {
                //fill the missing gap:
                CitiesArray.add(existingCityCounter, blankCityItem);
                //2 blanks at the end, fixes bug:
                CitiesArray.add(CitiesArray.size(), blankCityItem);
                CitiesArray.add(CitiesArray.size(), blankCityItem);
            }
            //2 greens at beginning:
            CitiesArray.add(0, greenBlankItem);
            CitiesArray.add(0, greenYesDownloaded);
        } else {
            CitiesArray.add(0, greenBlankItem);
            CitiesArray.add(0, greenNotDownloaded);

        }
    }


    private void initVarsAndHeaders() {

        existingCityCounter = 0;
        CitiesArray = new ArrayList<>();
        GlobalVars.detailMenuItems = new ArrayList<>();
        boneId_boneName = new ArrayList<>();

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.main_grid);
        bannerLayout = layoutInflater.inflate(R.layout.banner_layout, null);
        gridView.addHeaderView(bannerLayout);


    }


    private void setBanners(JSONObject jsonObj) {
        Button[] buttons = {b1, b2, b3, b4};
        ImageView[] images = {image1, image2, image3, image4};
        LinearLayout[] layouts = {layout1, layout2, layout3, layout4};
        // Getting data JSON Array nodes
        GlobalVars.BannersArray = new ArrayList<>();
        JSONArray data = null;
        try {
            data = jsonObj.getJSONArray("banners");
            // looping through All nodes
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                String text = item.getString("text");
                String imageBIG = item.getString("imageBIG");
                String urlAndroid = item.getString("UrlAndroid");

                final BannersModel bannerItem = new BannersModel();

                bannerItem.setText(text);
                bannerItem.setImageBIG(imageBIG);
                bannerItem.setUrlAndroid(urlAndroid);

                GlobalVars.BannersArray.add(bannerItem);

                int buttonID = getResources().getIdentifier("banner_button" + (i + 1), "id", getPackageName());
                buttons[i] = (Button) bannerLayout.findViewById(buttonID);
                buttons[i].setText(GlobalVars.BannersArray.get(i).getText());


                int imageID = getResources().getIdentifier("banner_image" + (i + 1), "id", getPackageName());
                images[i] = (ImageView) bannerLayout.findViewById(imageID);

                File file = new File(GlobalVars.getBasePath(getApplicationContext(), "Default_master/" + GlobalVars.BannersArray.get(i).getImageBIG()));
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    images[i].setImageBitmap(bitmap);
                }

                int layoutID = getResources().getIdentifier("banner_layout" + (i + 1), "id", getPackageName());
                layouts[i] = (LinearLayout) bannerLayout.findViewById(layoutID);
                layouts[i].setOnClickListener(new OnBannerClick(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class OnBannerClick implements View.OnClickListener {
        private int mPosition;

        OnBannerClick(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            Intent fullAdIntent = new Intent(MainGridActivity.this, FullAdActivity.class);
            fullAdIntent.putExtra("adNum", mPosition);
            startActivity(fullAdIntent);
        }
    }


    private void setCities(JSONObject jsonObj) {
        // Getting data JSON Array nodes
        try {
            JSONArray data = jsonObj.getJSONArray("cities");
            // looping through All nodes
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                String id = item.getString("id");
                String boneId = item.getString("boneId");
                String image = item.getString("image");
                String name = item.getString("name");


                final CitiesModel cityItem = new CitiesModel();

                cityItem.setId(id);
                cityItem.setBoneId(boneId);
                cityItem.setImage(image);
                cityItem.setName(name);

                //order the cities//
                boolean cityExists = dbTools.isDataAlreadyInDB(DBConstants.CITY_TABLE_NAME, DBConstants.cityId, cityItem.getId());
                if (!cityExists) {
                    CitiesArray.add(CitiesArray.size(), cityItem);
                } else {
                    CitiesArray.add(0, cityItem);
                    existingCityCounter++;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLongCityItemClick(final int mPosition) {
        final CitiesModel tempValues = CitiesArray.get(mPosition);

        //only delete if existant.
        if (dbTools.getData(DBConstants.CITY_TABLE_NAME, DBConstants.cityId, tempValues.getId()).getCount() > 0) {

            //show dialog//
            final Dialog dialog = new Dialog(MainGridActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_city_delete_dialog);

            Button startDeleteButton = (Button) dialog.findViewById(R.id.start_delete_btn);
            startDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dbTools.deleteWholeCity(tempValues.getId());


                    File[] files = getFilesDir().listFiles();
                    for (File file : files) {
                        if (file.getName().contains(tempValues.getId())) {
                            HelperMethods.deleteRecursive(file);
                        }
                    }

                    startActivity(new Intent(MainGridActivity.this, MainGridActivity.class));
                    finish();
                }

            });

            Button cancelDownloadButton = (Button) dialog.findViewById(R.id.cancel_delete_btn);
            cancelDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }


    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

    @Override
    public void onCityItemClick(final int position) {

        final CitiesModel tempValues = CitiesArray.get(position);

        for (int i = 0; i < GlobalVars.initDataModelArrayList.size(); i++) {
            if (GlobalVars.initDataModelArrayList.get(i).getCityId().equals(tempValues.getId())) {
                //save clicked cityId:
                myInstance.set_cityId(tempValues.getId());

                final String filePath = GlobalVars.initDataModelArrayList.get(i).getFile();
                updateDate = GlobalVars.initDataModelArrayList.get(i).getUpdate_date();
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                sourceZipFile = new File(GlobalVars.getBasePath(getApplicationContext(), fileName));    //download to here//
                destinationFolder = new File(GlobalVars.getBasePath(getApplicationContext(), fileName.substring(0, fileName.indexOf('.')))); //without .zip//

                //only download if non-existant.
                if (dbTools.getData(DBConstants.CITY_TABLE_NAME, DBConstants.cityId, tempValues.getId()).getCount() == 0) {

                    if (isNetworkOnline()) {
                        //show dialog//
                        final Dialog dialog = new Dialog(MainGridActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_city_download_dialog);


                        Button startDownloadButton = (Button) dialog.findViewById(R.id.start_download_btn);
                        startDownloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                                new DownloadFileAsync().execute(filePath, updateDate);
                            }

                        });

                        Button cancelDownloadButton = (Button) dialog.findViewById(R.id.cancel_download_btn);
                        cancelDownloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    } else {
                        final Dialog dialog = new Dialog(this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_no_connection_dialog);

                        Button okButton = (Button) dialog.findViewById(R.id.ok_btn);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        dialog.show();

                    }
                } else {
                    //city exists, diff date:
                    String cityDBDate = dbTools.getCellData(DBConstants.CITY_TABLE_NAME, DBConstants.dateUpdated, DBConstants.cityId, tempValues.getId());
                    if (!updateDate.equals(cityDBDate)) {

                        //show dialog, needs update//
                        final Dialog dialog = new Dialog(this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_city_update_dialog);

                        Window window = dialog.getWindow();
//                        window.setGravity(Gravity.BOTTOM);
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                        Button startUpdateButton = (Button) dialog.findViewById(R.id.update_download_btn);
                        startUpdateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (isNetworkOnline()) {
                                    dialog.dismiss();
                                    updatedCity = tempValues;
                                    isUpdate = true;
                                    new DownloadFileAsync().execute(filePath, updateDate);

                                } else {
                                    dialog.setContentView(R.layout.custom_no_connection_dialog);

                                    Button okButton = (Button) dialog.findViewById(R.id.ok_btn);
                                    okButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss();

                                        }
                                    });

                                    dialog.show();

                                }
                            }
                        });

                        dialog.show();

                    } else {
                        nextActivity(destinationFolder);
                    }
                }
                break;
            }
        }


    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {

        private ProgressDialog mProgressDialog;
        InputStream input;
        OutputStream output;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainGridActivity.this, R.style.full_screen_dialog);
            mProgressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            mProgressDialog.setMessage("מוריד...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ביטול", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DownloadFileAsync.this.cancel(true);
                    dialog.dismiss();
                }
            });
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... initData) {//filepath + date//
            int count;
            try {

                URL url = new URL(initData[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                input = new BufferedInputStream(url.openStream());
                output = new FileOutputStream(sourceZipFile);
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);

                    if (isCancelled()) {
                        output.flush();
                        output.close();
                        input.close();
                        return null;
                    }
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (isUpdate) {
                dbTools.deleteWholeCityLeaveFavorites(updatedCity.getId());
            }
            mProgressDialog.dismiss();
            new SavingFilesAsync().execute();

        }

        @Override
        protected void onCancelled() {
            try {
                output.flush();
                output.close();
//                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            delete files on cancel:
            finally {
                new File(GlobalVars.getBasePath(getApplicationContext(), fileName)).delete();
                new File(GlobalVars.getBasePath(getApplicationContext(), String.valueOf(destinationFolder))).delete();
                mProgressDialog.dismiss();
            }

        }
    }


    class SavingFilesAsync extends AsyncTask<String, String, String> {

        private ProgressDialog mSavingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSavingDialog = new ProgressDialog(MainGridActivity.this, R.style.full_screen_dialog);
            mSavingDialog.setMessage("שומר תוכן");
            mSavingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mSavingDialog.setCancelable(true);
            mSavingDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ביטול", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SavingFilesAsync.this.cancel(true);
                    dialog.dismiss();
                }
            });
            mSavingDialog.show();
        }

        @Override
        protected String doInBackground(String... initData) {//filepath + date//
            try {
                HelperMethods.unzip(sourceZipFile, destinationFolder);
                new File(GlobalVars.getBasePath(getApplicationContext(), fileName)).delete();
                setDBdata();
                mSavingDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            mSavingDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            mSavingDialog.dismiss();
            if (isUpdate) {
                nextActivity(destinationFolder);
            } else {
                startActivity(new Intent(MainGridActivity.this, MainGridActivity.class));
                finish();
            }
        }

        @Override
        protected void onCancelled() {
//            delete downloaded zip file on cancel:
            mSavingDialog.show();
            new File(GlobalVars.getBasePath(getApplicationContext(), fileName)).delete();
            new File(GlobalVars.getBasePath(getApplicationContext(), String.valueOf(destinationFolder))).delete();
            mSavingDialog.dismiss();

        }
    }


    private void setDBdata() {

        JSONObject cityObject = new JSONObject();
        File dir = new File(destinationFolder.toString());
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            try {
                for (File child : directoryListing) {

                    // loop thru json files in city directory//

                    if (child.toString().contains("CityCoordinates")) {
                        JSONObject jsonCityCoordinates = HelperMethods.loadJsonDataFromFile(child.toString());
                        JSONObject fullObject = jsonCityCoordinates.getJSONObject("cityCoordinates");
                        cityObject.put(DBConstants.cityId, myInstance.get_cityId());
                        cityObject.put(DBConstants.hebrewName, fullObject.getString("name"));
                        cityObject.put(DBConstants.name, fullObject.getString("EnglishName"));
                        cityObject.put(DBConstants.centerCoordinateLat, fullObject.getString("latitude"));
                        cityObject.put(DBConstants.centerCoordinateLon, fullObject.getString("longitude"));
                    } else if (child.toString().contains("StopsArticle")) {
                        JSONObject jsonStopsArticle = HelperMethods.loadJsonDataFromFile(child.toString());
                        cityObject.put(DBConstants.stopsArticle, jsonStopsArticle.toString());
                    } else if (child.toString().contains("TuristArticalsList")) {
                        JSONObject jsonTuristArticalsList = HelperMethods.loadJsonDataFromFile(child.toString());
                        cityObject.put(DBConstants.touristArticlesList, jsonTuristArticalsList.toString());
                    } else if (child.toString().contains("PlacesList")) {
                        JSONObject jsonPlace = HelperMethods.loadJsonDataFromFile(child.toString());
                        JSONArray data = jsonPlace.getJSONArray("places");
                        // looping through All nodes of json file:
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            item.put(DBConstants.cityId, myInstance.get_cityId());
                            dbTools.insertPlaceTable(item);
                        }
                    } else if (child.toString().contains("ArticalsList")) {
                        JSONObject jsonArticle = HelperMethods.loadJsonDataFromFile(child.toString());
                        JSONArray data = jsonArticle.getJSONArray("articles");
                        // looping through All nodes of json file:
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            item.put(DBConstants.cityId, myInstance.get_cityId());
                            dbTools.insertArticleTable(item);
                        }
                    }


                }
                //on purpose after the whole first file loop, adding to the table data//
                for (File child : directoryListing) {

                    if (child.toString().contains("_menu")) {
                        JSONObject jsonMenuList = HelperMethods.loadJsonDataFromFile(child.toString());
                        JSONArray data = jsonMenuList.getJSONArray("menu");
                        for (int i = 0; i < data.length() - 1; i++) {
                            JSONObject item = data.getJSONObject(i);

                            boneId_boneName.add(new Pair<>(item.getString("boneId"), item.getString("name")));

                        }
                    }
                }

                for (File child : directoryListing) {

                    if (child.toString().contains("PlacesCoordinatesList")) {
                        JSONObject jsonPlacesCoordinatesList = HelperMethods.loadJsonDataFromFile(child.toString());
                        JSONArray data = jsonPlacesCoordinatesList.getJSONArray("cityPlcesCoordinates");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);

                            String boneId = child.toString();
                            boneId = boneId.substring(boneId.lastIndexOf("/") + 1);
                            boneId = boneId.substring(5, 9);


                            for (int j = 0; j < boneId_boneName.size(); j++) {
                                if (boneId.equals(boneId_boneName.get(j).first)) {
                                    item.put(DBConstants.boneCategoryName, boneId_boneName.get(j).second);
                                    item.put(DBConstants.boneCategoryId, j + 1);
                                }
                            }


                            dbTools.addDataToTable(DBConstants.PLACE_TABLE_NAME, item, boneId);
                        }
                    }

                }

                //add extra important data//
                cityObject.put(DBConstants.dateUpdated, updateDate);
                cityObject.put(DBConstants.cityFolderPath, destinationFolder);

                dbTools.insertCityTable(cityObject);
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }


    private void nextActivity(File file) {
        Intent cityFolderNameIntent = new Intent(MainGridActivity.this, Detail_City_Activity.class);
        cityFolderNameIntent.putExtra("cityFolderName", file.toString());
        startActivity(cityFolderNameIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        myInstance.setCityName("עכבר עולם");
        myInstance.set_cityId(null);
        myInstance.set_boneIdTitle(null);
    }

}