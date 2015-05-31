package com.comrax.mouseappandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class DBTools extends SQLiteOpenHelper {


    public DBTools(Context applicationContext){
        super(applicationContext, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ARTICLE_TABLE = "CREATE TABLE "
                + DBConstants.ARTICLE_TABLE_NAME + " ("
                + DBConstants.boneId + " INTEGER, "
                + DBConstants.cityId + " INTEGER, "
                + DBConstants.imagePath + " TEXT, "
                + DBConstants.menuItemId + " INTEGER, "
                + DBConstants.name + " TEXT, "
                + DBConstants.nsId + " INTEGER, "
                + DBConstants.objId + " INTEGER, "
                + DBConstants.rating + " INTEGER, "
                + DBConstants.ratingCount + " INTEGER, "
                + DBConstants.responses + " TEXT, "
                + DBConstants.title + " TEXT, "
                + DBConstants.url + " TEXT, "
                + DBConstants.urlContent + " TEXT " + ");";


        String CREATE_CITY_TABLE = "CREATE TABLE "
                + DBConstants.CITY_TABLE_NAME + " ("
                + DBConstants.centerCoordinateLat + " REAL, "
                + DBConstants.centerCoordinateLon + " REAL, "
                + DBConstants.cityFolderPath + " TEXT, "
                + DBConstants.coordinates + " TEXT, "
                + DBConstants.dateUpdated + " TEXT, "
                + DBConstants.imageName + " TEXT, "
                + DBConstants.index + " INTEGER, "
                + DBConstants.mainArticles + " TEXT, "
                + DBConstants.menu + " TEXT, "
                + DBConstants.name + " TEXT, "
                + DBConstants.serviceMenu + " TEXT, "
                + DBConstants.stopsArticle + " TEXT, "
                + DBConstants.touristArticles + " TEXT " + ");";


        String CREATE_PLACE_TABLE = "CREATE TABLE "
                + DBConstants.PLACE_TABLE_NAME + " ("
                + DBConstants.price + " INTEGER, "
                + DBConstants.boneId + " INTEGER, "
                + DBConstants.cityId + " INTEGER, "
                + DBConstants.nsId + " INTEGER, "
                + DBConstants.objId + " INTEGER, "
                + DBConstants.address + " TEXT, "
                + DBConstants.description + " TEXT, "
                + DBConstants.fullDescriptionBody + " TEXT, "
                + DBConstants.hebrewName + " TEXT, "
                + DBConstants.name + " TEXT, "
                + DBConstants.rating + " INTEGER, "
                + DBConstants.ratingCount + " INTEGER, "
                + DBConstants.phone + " TEXT, "
                + DBConstants.type + " TEXT, "
                + DBConstants.urlString + " TEXT, "
                + DBConstants.userComments + " TEXT "
                + ");";


        String CREATE_PLACE_FAVORITE_TABLE = "CREATE TABLE "
                + DBConstants.PLACE_FAVORITE_TABLE_NAME + " ("
                + DBConstants.address + " TEXT, "
                + DBConstants.categoryId + " INTEGER, "
                + DBConstants.description + " TEXT, "
                + DBConstants.index + " INTEGER, "
                + DBConstants.name + " TEXT, "
                + DBConstants.phone + " TEXT, "
                + DBConstants.price + " INTEGER, "
                + DBConstants.rating + " INTEGER, "
                + DBConstants.type + " TEXT, "
                + DBConstants.urlString + " TEXT " + ");";

        try {
            db.execSQL(CREATE_PLACE_FAVORITE_TABLE);
            db.execSQL(CREATE_PLACE_TABLE);
            db.execSQL(CREATE_ARTICLE_TABLE);
            db.execSQL(CREATE_CITY_TABLE);

        } catch (SQLiteException e) {
            Log.e("Create table exception:", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.ARTICLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.CITY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.PLACE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.PLACE_FAVORITE_TABLE_NAME);

        onCreate(db);
    }



    public void insertCityTable(HashMap<String, String> queryValues){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DBConstants.boneId, queryValues.get("nid"));
        values.put(DBConstants.cityId, queryValues.get("title"));
        values.put(DBConstants.imagePath, queryValues.get("country"));
        values.put(DBConstants.menuItemId, queryValues.get("country"));
        values.put(DBConstants.name, queryValues.get("country"));
        values.put(DBConstants.nsId, queryValues.get("country"));
        values.put(DBConstants.objId, queryValues.get("country"));
        values.put(DBConstants.rating, queryValues.get("country"));
        values.put(DBConstants.ratingCount, queryValues.get("country"));
        values.put(DBConstants.responses, queryValues.get("country"));
        values.put(DBConstants.title, queryValues.get("country"));
        values.put(DBConstants.url, queryValues.get("country"));
        values.put(DBConstants.urlContent, queryValues.get("country"));

        database.insert(DBConstants.CITY_TABLE_NAME, null, values);

        database.close();

    }

    public void insertArticleTable(HashMap<String, String> queryValues){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DBConstants.boneId, queryValues.get("nid"));
        values.put(DBConstants.cityId, queryValues.get("title"));
        values.put(DBConstants.imagePath, queryValues.get("country"));
        values.put(DBConstants.menuItemId, queryValues.get("country"));
        values.put(DBConstants.name, queryValues.get("country"));
        values.put(DBConstants.nsId, queryValues.get("country"));
        values.put(DBConstants.objId, queryValues.get("country"));
        values.put(DBConstants.rating, queryValues.get("country"));
        values.put(DBConstants.ratingCount, queryValues.get("country"));
        values.put(DBConstants.responses, queryValues.get("country"));
        values.put(DBConstants.title, queryValues.get("country"));
        values.put(DBConstants.url, queryValues.get("country"));
        values.put(DBConstants.urlContent, queryValues.get("country"));

        database.insert(DBConstants.ARTICLE_TABLE_NAME, null, values);

        database.close();

    }


    public void insertPlaceTable(HashMap<String, String> queryValues){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

//        values.put(DBConstants.cityId, queryValues.get(DBConstants.cityId));

        values.put(DBConstants.name, queryValues.get(DBConstants.name));
        values.put(DBConstants.boneId, queryValues.get(DBConstants.boneId));
        values.put(DBConstants.nsId, queryValues.get(DBConstants.nsId));
        values.put(DBConstants.objId, queryValues.get(DBConstants.objId));

        values.put(DBConstants.urlString, queryValues.get(DBConstants.urlString));
        values.put(DBConstants.description, queryValues.get(DBConstants.description));
        values.put(DBConstants.address, queryValues.get(DBConstants.address));
        values.put(DBConstants.phone, queryValues.get(DBConstants.phone));
        values.put(DBConstants.type, queryValues.get(DBConstants.type));
        values.put(DBConstants.rating, queryValues.get(DBConstants.rating));
        values.put(DBConstants.ratingCount, queryValues.get(DBConstants.ratingCount));

        values.put(DBConstants.price, queryValues.get(DBConstants.price));
        values.put(DBConstants.fullDescriptionBody, queryValues.get(DBConstants.fullDescriptionBody));
        values.put(DBConstants.hebrewName, queryValues.get(DBConstants.hebrewName));
        values.put(DBConstants.userComments, queryValues.get(DBConstants.userComments));

        database.insert(DBConstants.PLACE_TABLE_NAME, null, values);

        database.close();

    }



}
