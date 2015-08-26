package com.mouse.world.activities_N_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mouse.world.R;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.database.DBConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by bez on 15/07/2015.
 */
public class ArticleActivity extends MyBaseDrawerActivity {

    private JSONObject urlContent;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.article_layout;
    }

    @Override
    protected String getTextForAppBar() {
        return myInstance.getCityName();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String info = getIntent().getStringExtra("articleData");

        try {
            urlContent = new JSONObject(info);

            ImageView topImage = (ImageView) findViewById(R.id.open_article_topImage);
            TextView descPhoto = (TextView) findViewById(R.id.open_article_descPhoto);
            TextView title = (TextView) findViewById(R.id.open_article_title);
            TextView dateAndCredit = (TextView) findViewById(R.id.open_article_dateAndCredit);
            TextView description = (TextView) findViewById(R.id.open_article_description);


            File file = new File(myInstance.get_cityFolderName() + "/" + urlContent.getString("image"));
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                topImage.setImageBitmap(bitmap);
            } else {
                topImage.setVisibility(View.GONE);
            }

            descPhoto.setText(urlContent.getString("descPhoto"));
            title.setText(urlContent.getString("nameTitle"));
            dateAndCredit.setText(urlContent.getString("dateAndCredit"));
            description.setText(urlContent.getString("description"));

            JSONArray jsonArray = new JSONArray(urlContent.getString("responses"));


            if (jsonArray.length() > 0) {

                TextView responsesTitle = (TextView)findViewById(R.id.detailed_article_responses_title);
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

            WebView mWebView = (WebView) findViewById(R.id.web_view_article);

            WebSettings settings = mWebView.getSettings();
            settings.setDefaultTextEncodingName("utf-8");

            String content = urlContent.getString("content");

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

            String str = myInstance.get_cityFolderName() + "/Images/";

            String fromStr = "src=\"Images/";
            String toStr = String.format("src=\"file://%s", str);

            html = html.replace(fromStr, toStr);

            sb.append(html);

            mWebView.loadDataWithBaseURL(null, sb.toString(), null, "UTF-8", null);


            mWebView.setWebViewClient(new WebViewClient() {
                // you tell the webclient you want to catch when a url is about to load
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.wtf("url_clicked", url);

                    if (url.contains("CM.world_place")) {
                        String first = url.substring(url.indexOf(",") + 1);
                        myInstance.set_boneId(first.substring(0, first.indexOf(",")));
                        String second = first.substring(first.indexOf(",") + 1);
                        myInstance.set_nsId(second.substring(0, second.indexOf(",")));
                        String third = second.substring(second.indexOf(",") + 1);
                        myInstance.set_objId(third.substring(0, third.indexOf(",")));

                        Intent placeActivity = new Intent(getApplicationContext(), PlaceActivity.class);
                        startActivity(placeActivity);

                        return true;
                    } else if (url.contains("CM.world_articles_item")) {
                        String first = url.substring(url.indexOf(",") + 1);
                        String second = first.substring(first.indexOf(",") + 1);
                        String third = second.substring(second.indexOf(",") + 1);

                        String article_obj = third.substring(0, third.indexOf(","));


                        String articleContent = dbTools.getCellData(DBConstants.ARTICLE_TABLE_NAME, DBConstants.urlContent, DBConstants.cityId, myInstance.get_cityId(), DBConstants.objId, article_obj);

                        if (articleContent != null) {
                            Intent articleIntent = new Intent(getApplicationContext(), ArticleActivity.class);
                            articleIntent.putExtra("articleData", articleContent);
                            startActivity(articleIntent);
                        } else {
                            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        }
                    } else if (url.contains("CM.city")) {

                        String first = url.substring(url.indexOf(",") + 1);
                        String cityId = first.substring(0, first.indexOf(","));

                        if (cityId.equals(myInstance.get_cityId())) {
                            Intent activityIntent = new Intent(getApplicationContext(), Detail_City_Activity.class);
                            activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(activityIntent);
                        } else {
                            Intent activityIntent = new Intent(getApplicationContext(), MainGridActivity.class);
                            activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Toast.makeText(getApplicationContext(), "בחר/הורד עיר", Toast.LENGTH_LONG).show();
                            startActivity(activityIntent);
                        }
                    } else {
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }

                    return true;
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    //on init load webView with html links//
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


        setFooterComment();

        setFooterFav();

        setFooterAd();

    }


    private void setFooterFav() {
        Button favoritesBtn = (Button) findViewById(R.id.footer_prefs_button);
        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(ArticleActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_fav_dialog);

                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button addFavButton = (Button) dialog.findViewById(R.id.add_fav_btn);
                addFavButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbTools.insertArticleFavorite(urlContent);
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

    private void setFooterComment() {
        Button btnComment = (Button) findViewById(R.id.footer_comment_btn);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArticleActivity.this, AddCommentActivity.class));
            }
        });

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


}