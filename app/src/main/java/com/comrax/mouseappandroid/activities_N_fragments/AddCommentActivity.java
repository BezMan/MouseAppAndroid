package com.mouse.world.activities_N_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mouse.world.R;
import com.mouse.world.app.App;
import com.mouse.world.http.RequestTask;
import com.mouse.world.http.RequestTaskDelegate;
import com.mouse.world.http.RequestTaskPOST;

/**
 * Created by bez on 13/07/2015.
 */
public class AddCommentActivity extends Activity implements RequestTaskDelegate {

    EditText nameEditText, messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comment_layout);


        Button btnClose = (Button) findViewById(R.id.close_comment_activity_btn);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Button btnSend = (Button) findViewById(R.id.send_message_btn);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        nameEditText = (EditText) findViewById(R.id.edit_name);


        messageEditText = (EditText) findViewById(R.id.edit_message);

        messageEditText.setHorizontallyScrolling(false);
        messageEditText.setMaxLines(Integer.MAX_VALUE);

        messageEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    handled = true;
                }
                return handled;
            }
        });

    }

    private void sendMessage() {

        if (nameEditText.getText().length()<2) {
            Toast.makeText(getApplicationContext(), "שדה 'שם' חובה", Toast.LENGTH_LONG).show();
        }
        else if (messageEditText.getText().length()<2) {
            Toast.makeText(getApplicationContext(), "שדה 'הודעה' חובה", Toast.LENGTH_LONG).show();
        }
        else {
            String urlString = String.format("http://www.mouse.co.il/appMouseWorldServiceRequest.ashx?appName=master@mouse.co.il&method=addNewResponses&" +
                    "boneId=%s" + "&nsId=%s" + "&objId=%s", App.getInstance().get_boneId(), App.getInstance().get_nsId(), App.getInstance().get_objId());


            String postParams = String.format("{\"name\":\"%s\"" + "," + " \"message\":\"%s\"}", nameEditText.getText(), messageEditText.getText());

            Log.wtf("urlString: ", "" + urlString);
            Log.wtf("postParams: ", "" + postParams);

            new RequestTaskPOST(this).execute(urlString, postParams);

        }
    }


    @Override
    public void onTaskPOSTCompleted(String result, RequestTask task) {
        Toast.makeText(getApplicationContext(), "ההודעה נשלחה בהצלחה", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onTaskGETCompleted(String result, RequestTask task) {

    }


}
