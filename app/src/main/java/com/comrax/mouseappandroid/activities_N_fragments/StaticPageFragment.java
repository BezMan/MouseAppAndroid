package com.mouse.world.activities_N_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.mouse.world.R;

import org.json.JSONException;
import org.json.JSONObject;

public class StaticPageFragment extends Fragment {

    private Bundle bundle;
    private TextView barTitleTextView;
    private String originalTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
    }

    @Override
    public void onResume() {
        super.onResume();
        barTitleTextView = (TextView) getActivity().findViewById(R.id.title_text);
        originalTitle = barTitleTextView.getText().toString();
        barTitleTextView.setText(bundle.getString("barTitle", null));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_static_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = (TextView)getActivity().findViewById(R.id.static_page_title);
        WebView mWebView = (WebView)getActivity().findViewById(R.id.web_view_static);

        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        try {
            JSONObject jsonObject = new JSONObject(bundle.getString("data", null));
            title.setText(jsonObject.getString("Title"));
            String content = jsonObject.getString("Content");

            String formattedContent = Html.fromHtml((String)content).toString();
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        barTitleTextView.setText(originalTitle);
    }
}
