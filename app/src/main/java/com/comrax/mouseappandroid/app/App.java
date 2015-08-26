package com.mouse.world.app;

import android.app.Application;

/**
 * Created by bez on 12/05/2015.
 */
public class App extends Application {
    private String _cityId;
    private String _boneId;
    private String _objId;
    private String _nsId;
    private String _cityFolderName;
    private String _boneIdTitle;
    private int BoneCategoryName;
    private String CityName;

    private boolean toggleGPS=true;


    private static App _instance;

    private App() {

    }

    public static App getInstance() {
        if(_instance == null) {
            _instance = new App();
        }
        return _instance;
    }

    public boolean isToggleGPS() {
        return toggleGPS;
    }

    public void setToggleGPS(boolean toggleGPS) {
        this.toggleGPS = toggleGPS;
    }

    public String get_nsId() {
        return _nsId;
    }

    public void set_nsId(String _nsId) {
        this._nsId = _nsId;
    }

    public int getBonePosition() {
        return BoneCategoryName;
    }

    public void setBonePosition(int boneCategoryName) {
        BoneCategoryName = boneCategoryName;
    }

    public void set_cityId(String cityId) {
        _cityId = cityId;
    }

    public String get_cityId() {
        return _cityId;
    }

    public void set_boneId(String boneId) {
        _boneId = boneId;
    }

    public String get_boneId() {
        return _boneId;
    }

    public String get_objId() {
        return _objId;
    }

    public void set_objId(String objId) {
        _objId = objId;
    }

    public String get_cityFolderName() {
        return _cityFolderName;
    }

    public void set_cityFolderName(String cityFolderName) {_cityFolderName = cityFolderName; }

    public String get_boneIdTitle() {
        return _boneIdTitle;
    }

    public void set_boneIdTitle(String boneIdTitle) {
        _boneIdTitle = boneIdTitle;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//
//            @Override
//            public void uncaughtException(Thread thread, Throwable ex) {
//                handleUncaughtException(thread, ex);
//            }
//        });
//    }
//
//    public void handleUncaughtException(Thread thread, Throwable throwable) {
//        throwable.printStackTrace();
//
//        try {
//            //String ex = String.format("%s: %s", throwable.getCause(), throwable.getMessage());
//            String ex = String.format("%s: %s, Line: %d",
//                    throwable.getClass().getName(),
//                    throwable.getMessage(),
//                    throwable.getStackTrace()[0].getLineNumber());
//
//            final Intent intent = new Intent(Intent.ACTION_SEND);
//            //Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//            intent.setType("plain/text");
//            //intent.setType("message/rfc822");
//            //sendIntent.setData(Uri.parse("comraxepad@gmail.com"));
//            //sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "silverbez@gmail.com" });
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Mouse App Exception");
//            intent.putExtra(Intent.EXTRA_TEXT, ex);
//
//            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            Intent mailer = Intent.createChooser(intent, null);
//            mailer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//
//            getApplicationContext().startActivity(mailer);
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//    }


}
