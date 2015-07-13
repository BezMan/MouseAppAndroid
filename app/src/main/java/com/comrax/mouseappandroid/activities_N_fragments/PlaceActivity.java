package com.comrax.mouseappandroid.activities_N_fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.comrax.mouseappandroid.R;
import com.comrax.mouseappandroid.app.App;
import com.comrax.mouseappandroid.database.DBConstants;
import com.comrax.mouseappandroid.database.DBTools;
import com.comrax.mouseappandroid.http.RequestTask;
import com.comrax.mouseappandroid.http.RequestTaskDelegate;

import java.io.File;

/**
 * Created by bez on 12/07/2015.
 */
public class PlaceActivity extends MyBaseDrawerActivity implements RequestTaskDelegate {

    int myRating;
    TextView dareg;

    String imagePath, name, hebName, description, address, type,
            phone, activityHours, publicTransportation, responses;

    Bundle bundle;

    Button b1, b2, b3, b4;
    ImageView image1, image2, image3, image4;
    LinearLayout layout1, layout2, layout3, layout4;

    RatingBar rating;

    DBTools dbTools;
    Cursor cursor;




    @Override
    protected int getLayoutResourceId() {
        return R.layout.place_full_layout;
    }

    @Override
    protected String getTextForAppBar() {
        return App.getInstance().getCityName();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbTools = new DBTools(this);
        bundle = getIntent().getExtras();
        cursor = dbTools.getData(DBConstants.PLACE_TABLE_NAME, DBConstants.name, bundle.getString(DBConstants.name, null), DBConstants.objId, bundle.getString(DBConstants.objId, null));

        setUpperData();

        setGraysWithExtras();

        setServiceItems();

        setFooterAd();
    }

    private void setUpperData() {
        imagePath = cursor.getString(cursor.getColumnIndex(DBConstants.image));
        name = cursor.getString(cursor.getColumnIndex(DBConstants.name));
        hebName = cursor.getString(cursor.getColumnIndex(DBConstants.hebrewName));
        description = cursor.getString(cursor.getColumnIndex(DBConstants.description));
        address = cursor.getString(cursor.getColumnIndex(DBConstants.address));
        type = cursor.getString(cursor.getColumnIndex(DBConstants.type));
        phone = cursor.getString(cursor.getColumnIndex(DBConstants.phone));
        activityHours = cursor.getString(cursor.getColumnIndex(DBConstants.activityHours));
        publicTransportation = cursor.getString(cursor.getColumnIndex(DBConstants.publicTransportation));
        responses = cursor.getString(cursor.getColumnIndex(DBConstants.responses));

        String fullDescription = new StringBuilder().append("<![CDATA[")
                .append("<html><head><style>")
                .append("body{font-family:arial;font-size:17px;direction:rtl;background:none;}")
                .append("</style></head><body>")
                .append(description)
                .append("</body></html>")
                .append("]]>")
                .toString();


        ImageView headImageView = (ImageView) findViewById(R.id.detailed_place_head_imageView);


        File file = new File(App.getInstance().get_cityFolderName() + "/" + imagePath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            headImageView.setImageBitmap(bitmap);
        }

        TextView titleView = (TextView) findViewById(R.id.detailed_place_english_title);
        titleView.setText(name);

        TextView hebTitleView = (TextView) findViewById(R.id.detailed_place_hebrew_title);
        hebTitleView.setText(hebName);

        TextView addressView = (TextView) findViewById(R.id.detailed_place_address);
        addressView.setText(address);

        TextView mainDetailedText = (TextView) findViewById(R.id.detailed_place_main_text);
        String html = Html.fromHtml(fullDescription).toString();
        String html2 = Html.fromHtml(html).toString();

        mainDetailedText.setText(Html.fromHtml(html2));


        rating = (RatingBar) findViewById(R.id.open_details_item_ratingBar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                Toast.makeText(getApplicationContext(), "rating: " + ratingBar.getRating(), Toast.LENGTH_LONG).show();
                myRating = (int) ratingBar.getRating();
            }
        });


//        dareg= (TextView)findViewById(R.id.open_details_item_dareg_textBtn);
//        dareg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                daregClicked();
//            }
//        });

        Button favoritesBtn = (Button) findViewById(R.id.detailed_place_prefs_button);
        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbTools.insertFavorite(cursor);
                Toast.makeText(getApplicationContext(), "נשמר בהצלחה", Toast.LENGTH_LONG).show();

            }

        });
    }


    private void setGraysWithExtras() {

        TextView phoneTitle = (TextView) findViewById(R.id.detailed_place_phone_title);
        TextView activityHoursTitle = (TextView) findViewById(R.id.detailed_place_activity_hours_title);
        TextView publicTransportationTitle = (TextView) findViewById(R.id.detailed_place_public_transportation_title);
        TextView responsesTitle = (TextView) findViewById(R.id.detailed_place_responses_title);

        LinearLayout phoneLayout = (LinearLayout) findViewById(R.id.detailed_place_phone_layout);


        final TextView phoneView = (TextView) findViewById(R.id.detailed_place_phone_num);
        TextView activityHoursView = (TextView) findViewById(R.id.detailed_place_activity_hours);
        TextView publicTransportationView = (TextView) findViewById(R.id.detailed_place_public_transportation);
        TextView responsesView = (TextView) findViewById(R.id.detailed_place_responses);


//        phoneLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:+" + phoneView.getText().toString().trim()));
//                startActivity(callIntent);
//            }
//        });
//

        if (!phone.equals("")) {
            phoneTitle.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.VISIBLE);
            phoneView.setText(phone);

        }

        if (!activityHours.equals("")) {
            activityHoursTitle.setVisibility(View.VISIBLE);
            activityHoursView.setVisibility(View.VISIBLE);
            activityHoursView.setText(activityHours);

        }

        if (!publicTransportation.equals("")) {
            publicTransportationTitle.setVisibility(View.VISIBLE);
            publicTransportationView.setVisibility(View.VISIBLE);
            publicTransportationView.setText(publicTransportation);

        }

        if (!responses.equals("[]")) {
            responsesTitle.setVisibility(View.VISIBLE);
            responsesView.setVisibility(View.VISIBLE);
            responsesView.setText(responses);

        }
    }


    private void setFooterAd() {
        ImageView imageButton = (ImageView) findViewById(R.id.footer_item_ad);
        int rand = (int) (Math.random() * 10 + 1);

        File file = new File("/sdcard/Mouse_App/Default_master/Images/MenuIcons/" + "320x50_" + rand + ".jpg");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageButton.setImageBitmap(bitmap);
        }

    }


    private void setServiceItems() {

        TextView[] textViews = {b1, b2, b3, b4};
        ImageView[] images = {image1, image2, image3, image4};
        LinearLayout[] layouts = {layout1, layout2, layout3, layout4};


        for (int i = 0; i < MainGridActivity.BannersArray.size(); i++) {

            int buttonID = getResources().getIdentifier("footer_item_title_" + (i + 1), "id", getPackageName());
            textViews[i] = (TextView) findViewById(buttonID);
            textViews[i].setText(MainGridActivity.BannersArray.get(i).getText());


            int imageID = getResources().getIdentifier("footer_item_image_" + (i + 1), "id", getPackageName());
            images[i] = (ImageView) findViewById(imageID);

            File file = new File("/sdcard/Mouse_App/Default_master/" + MainGridActivity.BannersArray.get(i).getImageBIG());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                images[i].setImageBitmap(bitmap);
            }

            int layoutID = getResources().getIdentifier("footer_item_layout_" + (i + 1), "id", getPackageName());
            layouts[i] = (LinearLayout) findViewById(layoutID);
            layouts[i].setOnClickListener(new OnBannerClick(i));
        }


    }

    @Override
    public void onTaskPOSTCompleted(String result, RequestTask task) {

    }

    @Override
    public void onTaskGETCompleted(String result, RequestTask task) {
        dareg.setVisibility(View.GONE);

    }


    private class OnBannerClick implements View.OnClickListener {
        private int mPosition;

        OnBannerClick(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MainGridActivity.BannersArray.get(mPosition).getUrlAndroid()));
            startActivity(browserIntent);
        }
    }


//    public void daregClicked(){
//    String url = String.format("http://www.mouse.co.il/appMouseWorldServiceRequest.ashx?appName=master@mouse.co.il&method=addNewRate" +
//            "&rate=%d" +
//            "&boneId=%s" +
//            "&nsId=%s" +
//            "&objId=%s",
//            myRating, App.getInstance().get_boneId() , App.getInstance().get_nsId(), App.getInstance().get_objId()
//            ) ;
//    new RequestTaskGet(this).execute(url, null);
//        //onGet, change the DAREG btn look.
//
//    }



}