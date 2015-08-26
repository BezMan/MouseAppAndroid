package com.mouse.world.activities_N_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mouse.world.R;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.database.DBConstants;
import com.mouse.world.http.RequestTask;
import com.mouse.world.http.RequestTaskDelegate;
import com.mouse.world.http.RequestTaskGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by bez on 12/07/2015.
 */
public class PlaceActivity extends MyBaseDrawerActivity implements RequestTaskDelegate {

    private int myRating;
    private TextView boneTextView, dareg;

    public String imagePath, name, hebName, content, address, type,
            phone, activityHours, publicTransportation;


    public Button b1, b2, b3, b4;
    public ImageView image1, image2, image3, image4;
    public LinearLayout layout1, layout2, layout3, layout4;

    private RatingBar rating;

    private Cursor mCursor;

    private JSONArray jsonArray;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.place_full_layout;
    }

    @Override
    protected String getTextForAppBar() {
        return myInstance.getCityName();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = dbTools.getData(DBConstants.PLACE_TABLE_NAME, DBConstants.cityId, myInstance.get_cityId(), DBConstants.boneId, myInstance.get_boneId(), DBConstants.nsId, myInstance.get_nsId(), DBConstants.objId, myInstance.get_objId());
        mCursor=cursor;

        setBoneTitleAndColor();

        setUpperData();

        setGraysWithExtras();

        set2GreenButtons();

        setServiceItems();

        setFooterAd();
    }


    private void set2GreenButtons() {

        Button btnComment = (Button) findViewById(R.id.detailed_place_comment_btn);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlaceActivity.this, AddCommentActivity.class));
//                Toast.makeText(getApplicationContext(), "btn", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnEmailThem = (Button) findViewById(R.id.detailed_place_email_them_btn);
        btnEmailThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "customer-mouse@mouse.co.il", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "עדכנו אותנו - אפליקציית עכבר עולם");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "שם המקום: " + "\n" + name + "\n" + hebName + "\n\n" + "שם העיר: " + myInstance.getCityName());
                startActivity(Intent.createChooser(emailIntent, "Email עכבר העיר"));
            }
        });

    }


    private void setBoneTitleAndColor() {
        boneTextView = (TextView) findViewById(R.id.bone_title);
        int a = mCursor.getColumnIndex(DBConstants.boneCategoryName);
        String boneTitle = mCursor.getString(a);
        boneTextView.setText(boneTitle);

        int pos = mCursor.getInt(mCursor.getColumnIndex(DBConstants.boneCategoryId));
        if(pos>0)
            boneTextView.setBackgroundColor(GlobalVars.boneColors[pos-1]);


        boneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setUpperData() {
        imagePath = mCursor.getString(mCursor.getColumnIndex(DBConstants.image));
        name = mCursor.getString(mCursor.getColumnIndex(DBConstants.name));
        hebName = mCursor.getString(mCursor.getColumnIndex(DBConstants.hebrewName));
        content = mCursor.getString(mCursor.getColumnIndex(DBConstants.fullDescriptionBody));
        address = mCursor.getString(mCursor.getColumnIndex(DBConstants.address));
        type = mCursor.getString(mCursor.getColumnIndex(DBConstants.type));
        phone = mCursor.getString(mCursor.getColumnIndex(DBConstants.phone));
        activityHours = mCursor.getString(mCursor.getColumnIndex(DBConstants.activityHours));
        publicTransportation = mCursor.getString(mCursor.getColumnIndex(DBConstants.publicTransportation));
        try {
            jsonArray = new JSONArray(mCursor.getString(mCursor.getColumnIndex(DBConstants.responses)));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ImageView headImageView = (ImageView) findViewById(R.id.detailed_place_head_imageView);


        File file = new File(myInstance.get_cityFolderName() + "/" + imagePath);
        if ( !imagePath.equals("") && file.exists()) {
            headImageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            headImageView.setImageBitmap(bitmap);
        }

        TextView titleView = (TextView) findViewById(R.id.detailed_place_english_title);
        titleView.setText(name);

        TextView hebTitleView = (TextView) findViewById(R.id.detailed_place_hebrew_title);
        hebTitleView.setText(hebName);

        TextView addressView = (TextView) findViewById(R.id.detailed_place_address);
        addressView.setText(address);
        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + address));
                startActivity(navIntent);

            }
        });

        WebView mWebView = (WebView) findViewById(R.id.web_view_place);
        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");


        String formattedContent = Html.fromHtml((String) content).toString();
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><html><head>");
        sb.append("<meta charset='utf-8'>");
        sb.append("<style>\n" +
                "body{direction:rtl;font-family:arial !important;}\n" +
                "h3{text-align:center;}\n" +
                "iframe{display:none;}\n" +
                ".map_widget clearfix{width:304px;}\n" +
                "img{max-width:304px;border:none;}\n" +
                "</style>\n" +
                "<script>function onLoad(){document.getElementsByTagName('iframe')[0].setAttribute('width', '304');window.scrollTo(window.innerWidth, 0)}</script>\n" +
                "</head>");

        String html = String.format("<body>%s</body></html>", formattedContent);

        sb.append(html);

        mWebView.loadDataWithBaseURL(null, sb.toString(), null, "UTF-8", null);




        mWebView.setWebViewClient(new WebViewClient(){
            // you tell the webclient you want to catch when a url is about to load
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String  url){
                Log.wtf("url_clicked", url);

                if(url.contains("CM.world_place")){
                    String first = url.substring(url.indexOf(",") + 1);
                    myInstance.set_boneId(first.substring(0, first.indexOf(",")));
                    String second = first.substring(first.indexOf(",") + 1);
                    myInstance.set_nsId(second.substring(0, second.indexOf(",")));
                    String third = second.substring(second.indexOf(",") + 1);
                    myInstance.set_objId(third.substring(0, third.indexOf(",")));

//                    Bundle bundle = new Bundle();
//                    bundle.putString(DBConstants.cityId, myInstance.get_cityId());
//                    bundle.putString(DBConstants.objId, myInstance.get_objId());

                    Intent placeActivity = new Intent(getApplicationContext(), PlaceActivity.class);
//                    placeActivity.putExtras(bundle);
                    startActivity(placeActivity);

                    return true;
                }
                else if(url.contains("CM.world_articles_item")){
                    String first = url.substring(url.indexOf(",") + 1);
                    String second = first.substring(first.indexOf(",") + 1);
                    String third = second.substring(second.indexOf(",") + 1);

                    String article_obj = third.substring(0,third.indexOf(","));


                    String articleContent = dbTools.getCellData(DBConstants.ARTICLE_TABLE_NAME, DBConstants.urlContent, DBConstants.cityId, myInstance.get_cityId(), DBConstants.objId, article_obj);

                    if(articleContent!=null) {
                        Intent articleIntent = new Intent(getApplicationContext(), ArticleActivity.class);
                        articleIntent.putExtra("articleData", articleContent);
                        startActivity(articleIntent);
                    }
                    else{
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                }
                else if(url.contains("CM.city")) {

                    String first = url.substring(url.indexOf(",") + 1);
                    String cityId = first.substring(0,first.indexOf(","));

                    if(cityId.equals(myInstance.get_cityId())){
                        Intent activityIntent = new Intent(getApplicationContext(), Detail_City_Activity.class);
                        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(activityIntent);
                    }
                    else{
                        Intent activityIntent = new Intent(getApplicationContext(), MainGridActivity.class);
                        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Toast.makeText(getApplicationContext(), "בחר/הורד עיר", Toast.LENGTH_LONG).show();
                        startActivity(activityIntent);
                    }
                }

                else{
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }

                return true;
            }

            @Override
            public void onLoadResource(WebView  view, String  url){
                //on init load webView with html links//
            }
        });




    rating = (RatingBar) findViewById(R.id.open_details_item_ratingBar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                Toast.makeText(getApplicationContext(), "rating: " + ratingBar.getRating(), Toast.LENGTH_LONG).show();
                myRating = (int) ratingBar.getRating();
            }
        });


        dareg = (TextView) findViewById(R.id.open_details_item_dareg_textBtn);
        dareg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daregClicked();
            }
        });

        Button favoritesBtn = (Button) findViewById(R.id.detailed_place_prefs_button);
        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(PlaceActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_fav_dialog);

                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button addFavButton = (Button) dialog.findViewById(R.id.add_fav_btn);
                addFavButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbTools.insertPlaceFavorite(mCursor);
                        Toast.makeText(getApplicationContext(), "נשמר בהצלחה", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                Button cancelFavButton = (Button) dialog.findViewById(R.id.cancel_fav_btn);
                cancelFavButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

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
//        TextView responsesView = (TextView) findViewById(R.id.detailed_place_responses);


        if (phone.length() > 8) {
            phoneTitle.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.VISIBLE);
            phoneView.setText(phone);

            phoneLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+" + phoneView.getText().toString().trim()));
                    startActivity(callIntent);
                }
            });

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

        if (jsonArray.length() > 0) {

            responsesTitle.setVisibility(View.VISIBLE);

            LinearLayout mLinearListView = (LinearLayout) findViewById(R.id.linear_listview);
            /***
             * adding item into listview
             */
            for (int i = 0; i < jsonArray.length(); i++) {
                /**
                 * inflate items/ add items in linear layout instead of listview
                 */
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView = inflater.inflate(R.layout.response_item_layout, null);
                /**
                 * getting id of response_item_layout.xml
                 */
                TextView mFirstNameView = (TextView) mLinearView.findViewById(R.id.response_message);
                TextView namePlusDateView = (TextView) mLinearView.findViewById(R.id.response_namePlusDate);

                /**
                 * set item into row
                 */
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    final String message = jsonObject.getString("message");
                    final String userName = jsonObject.getString("userName");
                    final String date = jsonObject.getString("date");
                    mFirstNameView.setText(message);
                    namePlusDateView.setText(userName + " " + date);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /**
                 * add view in top linear
                 */

                mLinearListView.addView(mLinearView);
            }
        }

    }















    private void setFooterAd() {
        ImageView imageButton = (ImageView) findViewById(R.id.footer_item_ad);
        int rand = (int) (Math.random() * 10 + 1);

        File file = new File(GlobalVars.getBasePath(getApplicationContext(), GlobalVars.IconFolder + "320x50_" + rand + ".jpg"));
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageButton.setImageBitmap(bitmap);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalVars.BannersArray.get(0).getUrlAndroid()));
                startActivity(browserIntent);
            }
        });

    }


    private void setServiceItems() {

        TextView[] textViews = {b1, b2, b3, b4};
        ImageView[] images = {image1, image2, image3, image4};
        LinearLayout[] layouts = {layout1, layout2, layout3, layout4};


        for (int i = 0; i < GlobalVars.BannersArray.size(); i++) {

            int buttonID = getResources().getIdentifier("footer_item_title_" + (i + 1), "id", getPackageName());
            textViews[i] = (TextView) findViewById(buttonID);
            textViews[i].setText(GlobalVars.BannersArray.get(i).getText());


            int imageID = getResources().getIdentifier("footer_item_image_" + (i + 1), "id", getPackageName());
            images[i] = (ImageView) findViewById(imageID);

            File file = new File(GlobalVars.getBasePath(getApplicationContext(), "Default_master/" + GlobalVars.BannersArray.get(i).getImageBIG()));
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
        Log.wtf("task", "" + task);
        dareg.setVisibility(View.GONE);

    }


    private class OnBannerClick implements View.OnClickListener {
        private int mPosition;

        OnBannerClick(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            Intent fullAdIntent = new Intent(PlaceActivity.this, FullAdActivity.class);
            fullAdIntent.putExtra("adNum",mPosition);
            startActivity(fullAdIntent);
        }
    }


    public void daregClicked() {
        String url = String.format("http://www.mouse.co.il/appMouseWorldServiceRequest.ashx?appName=master@mouse.co.il&method=addNewRate" +
                        "&rate=%d" +
                        "&boneId=%s" +
                        "&nsId=%s" +
                        "&objId=%s",
                myRating, myInstance.get_boneId(), myInstance.get_nsId(), myInstance.get_objId()
        );
        if (myRating > 0)
            new RequestTaskGet(this).execute(url, null);
        //onGet, change the DAREG btn look.

    }

    @Override
    protected void onStop() {
        super.onStop();
        mCursor.close();
    }
}
