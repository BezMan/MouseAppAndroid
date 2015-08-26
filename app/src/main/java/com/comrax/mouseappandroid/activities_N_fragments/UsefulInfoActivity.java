package com.mouse.world.activities_N_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.mouse.world.R;
import com.mouse.world.database.DBConstants;

/**
 * Created by bez on 15/07/2015.
 */
public class UsefulInfoActivity extends MyBaseDrawerActivity {


    @Override
    protected int getLayoutResourceId() {
        return R.layout.useful_info_article_layout;
    }

    @Override
    protected String getTextForAppBar() {
        return myInstance.getCityName();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String theTitle = intent.getStringExtra("articleTitle");
        String content = intent.getStringExtra("articleHtmlContent");

        TextView title = (TextView) findViewById(R.id.uesful_info_title);
        title.setText(theTitle);

        WebView mWebView = (WebView) findViewById(R.id.web_view_article);
        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

//        String formattedContent = Html.fromHtml((String) content).toString();
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

        String html = String.format("<body>%s</body></html>", content);

        String str = myInstance.get_cityFolderName() + "/Images/";

        String fromStr = "src=\"Images/";
        String toStr = String.format("src=\"file://%s", str);

        html = html.replace(fromStr, toStr);

        sb.append(html);

        mWebView.loadDataWithBaseURL(null, sb.toString(), null, "UTF-8", null);




        mWebView.setWebViewClient(new WebViewClient(){
            // you tell the webclient you want to catch when a url is about to load
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String  url){
                Log.wtf("url_clicked", url);

                if(url.contains("CM.world_place")){
                    String first = url.substring(url.indexOf(",") + 1);
                    String second = first.substring(first.indexOf(",") + 1);
                    String third = second.substring(second.indexOf(",") + 1);

                    myInstance.set_objId(third.substring(0, third.indexOf(",")));

                    Intent placeActivity = new Intent(getApplicationContext(), PlaceActivity.class);
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
    }
}
