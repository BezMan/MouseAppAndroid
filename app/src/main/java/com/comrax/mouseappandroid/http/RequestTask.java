package com.mouse.world.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public abstract class RequestTask extends AsyncTask<String, Integer, String> {

    protected Context _context;
    protected RequestTaskDelegate _delegate;
    protected String _requestType;
    protected URL _url;

    public RequestTask(RequestTaskDelegate delegate) {
        _delegate = delegate;
        _context = (Context) delegate;
    }

    public void setUrl(URL url) {
        _url = url;
    }

    public URL getUrl() {
        return _url;
    }

    public static List<HttpCookie> getCookies() {
        CookieStore cookieStore = null;

        try {
            CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
            cookieStore = cookieManager.getCookieStore();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return cookieStore.getCookies();
    }

    public static boolean removeAllCookies() {
        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
        CookieStore cookieStore = cookieManager.getCookieStore();

        return cookieStore.removeAll();
    }



    @Override
    protected String doInBackground(String... params) {
        StringBuilder stringBuilder = null;

        try {
            String url = params[0];
            String postParams = null;
            if(params.length > 1) {
                postParams = params[1];
            }

            URL myUrl = new URL(url);
            setUrl(myUrl);

            Log.wtf("", myUrl.toString());

            trustAllHosts();

            HttpsURLConnection urlConnection = (HttpsURLConnection) myUrl.openConnection();

            urlConnection.setHostnameVerifier(DO_NOT_VERIFY);
            urlConnection.setRequestMethod(_requestType);
            String urlString = urlConnection.getURL().toString();

            //If made login request -> save the cookie
            if(_requestType == "POST") {
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                OutputStreamWriter os = new OutputStreamWriter(urlConnection.getOutputStream());
                os.write(postParams);
                os.flush();
                os.close();

                if(urlString.indexOf("login") != -1) {
                    CookieManager cookieManager = _getCookiesFromConnection(urlConnection);
                    CookieHandler.setDefault(cookieManager);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //String cookieString = cookieManager.getCookieStore().getCookies().get(0).toString();
//                    editor.putString(GlobalVars.COOKIE_PASSWORD, postParams);
                    editor.commit();
                }
            }

            InputStream ins = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in  = new BufferedReader(isr);

            String inputLine;
            stringBuilder = new StringBuilder();

            while((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            in.close();

        } catch (MalformedURLException e) {
            Log.e("MalformedURLException: ", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException: ", e.getMessage());
        } catch (RuntimeException e) {
            Log.e("RuntimeException: ", e.getMessage());
        }

        return String.valueOf(stringBuilder);
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - don't check for any certificate
     */
    protected static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected CookieManager _getCookiesFromConnection(HttpsURLConnection connection) {
        final String COOKIES_HEADER = "Set-Cookie";
        CookieManager cookieManager = new CookieManager();

        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if(cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }

        return cookieManager;
    }

    protected abstract void setRequestType(String RequestType);
}
