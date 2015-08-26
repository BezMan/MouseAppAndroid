package com.mouse.world.activities_N_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.mouse.world.R;
import com.mouse.world.app.GlobalVars;
import com.mouse.world.app.HelperMethods;
import com.mouse.world.model.InitDataModel;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class SplashActivity extends Activity {


    private String savedDateJson, requestStr;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //TODO: choose first option here: //
        savedDateJson = prefs.getString("masterDateJson", null);

//        savedDateJson=  "{ \"files\": [{\"id\": \"1\",\"CityId\": \"0\",\"File\": \"http://aws.comrax.com/mouse/Default_master.zip\",\"Update_date\": \"11/11/2014 18:02:13\"},{\"id\": \"2\",\"CityId\": \"1146\",\"File\": \"http://aws.comrax.com/mouse/London_master_1146.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"3\",\"CityId\": \"1174\",\"File\": \"http://aws.comrax.com/mouse/Rome_master_1174.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"4\",\"CityId\": \"1179\",\"File\": \"http://aws.comrax.com/mouse/NewYork_master_1179.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"5\",\"CityId\": \"1185\",\"File\": \"http://aws.comrax.com/mouse/Amsterdam_master_1185.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"6\",\"CityId\": \"1190\",\"File\": \"http://aws.comrax.com/mouse/Barcelona_master_1190.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"7\",\"CityId\": \"1193\",\"File\": \"http://aws.comrax.com/mouse/Berlin_master_1193.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"8\",\"CityId\": \"1197\",\"File\": \"http://aws.comrax.com/mouse/Prauge_master_1197.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"9\",\"CityId\": \"1372\",\"File\": \"http://aws.comrax.com/mouse/Budapest_master_1372.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"10\",\"CityId\": \"1448\",\"File\": \"http://aws.comrax.com/mouse/Madrid_master_1448.zip\",\"Update_date\": \"30/11/2014 18:02:13\"},{\"id\": \"11\",\"CityId\": \"1171\",\"File\": \"http://aws.comrax.com/mouse/Paris_master_1171.zip\",\"Update_date\": \"30/11/2014 18:02:13\"}]}";
//        savedDateJson=      "{ \"files\": [{\"id\": \"1\",\"CityId\": \"0\",\"File\": \"http://aws.comrax.com/mouse/Default_master.zip\",\"Update_date\": \"14/10/2014 11:43:05\"},{\"id\": \"2\",\"CityId\": \"1146\",\"File\": \"http://aws.comrax.com/mouse/London_master_1146.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"3\",\"CityId\": \"1174\",\"File\": \"http://aws.comrax.com/mouse/Rome_master_1174.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"4\",\"CityId\": \"1179\",\"File\": \"http://aws.comrax.com/mouse/NewYork_master_1179.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"5\",\"CityId\": \"1185\",\"File\": \"http://aws.comrax.com/mouse/Amsterdam_master_1185.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"6\",\"CityId\": \"1190\",\"File\": \"http://aws.comrax.com/mouse/Barcelona_master_1190.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"7\",\"CityId\": \"1193\",\"File\": \"http://aws.comrax.com/mouse/Berlin_master_1193.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"8\",\"CityId\": \"1197\",\"File\": \"http://aws.comrax.com/mouse/Prauge_master_1197.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"9\",\"CityId\": \"1372\",\"File\": \"http://aws.comrax.com/mouse/Budapest_master_1372.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"10\",\"CityId\": \"1448\",\"File\": \"http://aws.comrax.com/mouse/Madrid_master_1448.zip\",\"Update_date\": \"27/10/2014 11:43:05\"},{\"id\": \"11\",\"CityId\": \"1171\",\"File\": \"http://aws.comrax.com/mouse/Paris_master_1171.zip\",\"Update_date\": \"27/10/2014 11:43:05\"}]}";


        if (isNetworkOnline()) {
            new RequestTask().execute("http://www.mouse.co.il/appService.ashx?appName=master@mouse.co.il");
        } else {
            if (savedDateJson != null) {
                fillArray(savedDateJson);
                nextActivity();
            } else {
                Toast.makeText(getApplicationContext(), "network required, no data", Toast.LENGTH_LONG).show();
                finish();
            }

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


    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (Exception e) {
                fillArray(savedDateJson);
                nextActivity();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            requestStr = result;
            fillArray(requestStr);
            compareJsonVals(requestStr);

        }
    }


    private void compareJsonVals(String result) {

        try {
            if (savedDateJson != null) {
                if (!savedDateJson.equals(result)) {
                    //new json value exists:
                    JSONObject jsonObject = new JSONObject(savedDateJson);
                    JSONArray files = jsonObject.getJSONArray("files");

                    JSONObject masterItem = (JSONObject) files.get(0);
                    String masterDate = masterItem.getString("Update_date");

                    if (!GlobalVars.initDataModelArrayList.get(0).getUpdate_date().equals(masterDate)) {
//                        if new masterDate exists, we need to download it:
                        new DownloadFileAsync().execute(GlobalVars.initDataModelArrayList.get(0).getFile());
                    } else {
                        nextActivity();
                    }
                } else {
                    nextActivity();
                }
            } else {// no saved vals:
                new DownloadFileAsync().execute(GlobalVars.initDataModelArrayList.get(0).getFile());
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }


    }


    public void fillArray(String result) {
        try {


            JSONObject jsonObject = new JSONObject(result);
            JSONArray files = jsonObject.getJSONArray("files");

            GlobalVars.initDataModelArrayList = new ArrayList<>();

            for (int i = 0; i < files.length(); i++) {
                JSONObject jsonItem = (JSONObject) files.get(i);

                final InitDataModel arrayItem = new InitDataModel();

                /******* Firstly take data in model object ******/
                arrayItem.setCityId(jsonItem.getString("CityId"));
                arrayItem.setFile(jsonItem.getString("File"));
                arrayItem.setUpdate_date(jsonItem.getString("Update_date"));

                /******** Add Model Object in ArrayList **********/
                GlobalVars.initDataModelArrayList.add(arrayItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {

        private ProgressDialog mProgressDialog;
        String fileName;
        File sourceZipFile, destinationFolder;
        InputStream input;
        OutputStream output;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SplashActivity.this);
            mProgressDialog.setMessage("Downloading file..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DownloadFileAsync.this.cancel(true);
                    dialog.dismiss();
                }
            });
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            int count;
            String urlStr = urls[0];
            fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
            try {

                URL url = new URL(urlStr);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lengthOfFile = conexion.getContentLength();
                input = new BufferedInputStream(url.openStream());
                sourceZipFile = new File(GlobalVars.getBasePath(getApplicationContext(), fileName));    //download to here//
                destinationFolder = new File(GlobalVars.getBasePath(getApplicationContext(), fileName.substring(0, fileName.indexOf('.')))); //without .zip//

                output = new FileOutputStream(sourceZipFile);
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);

                    if (isCancelled())
                        break;
                }

                output.flush();
                output.close();
                input.close();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("masterDateJson", requestStr).commit();

            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Error saving file", Toast.LENGTH_LONG).show();
                cancel(true);
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            try {

                HelperMethods.unzip(sourceZipFile, destinationFolder);
                new File(GlobalVars.getBasePath(getApplicationContext(), fileName)).delete();

                mProgressDialog.dismiss();
                nextActivity();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error unzipping file", Toast.LENGTH_LONG).show();
            }
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

            finish();
        }

    }


    private void nextActivity() {

//        File[] files = getFilesDir().listFiles();
//        for (File file : files) {
//            Log.wtf("listFiles: ", file.getName() + "");
//        }

        startActivity(new Intent(SplashActivity.this, MainGridActivity.class));
        finish();

    }


}
