package com.mouse.world.activities_N_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mouse.world.R;
import com.mouse.world.app.GlobalVars;

public class FullAdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_ad);

        //get extra int (1-4), set ad layout background to image with specific name

        final int position = getIntent().getIntExtra("adNum", -1);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ad_background_layout);
        layout.setBackgroundResource(getResources().getIdentifier("com.mouse.world:drawable/" + "ad_" + (position + 1), null, null));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalVars.BannersArray.get(position).getUrlAndroid()));
                startActivity(browserIntent);
                finish();
            }
        });

        ImageView imageViewExit = (ImageView)findViewById(R.id.ad_exit_button);
        imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
