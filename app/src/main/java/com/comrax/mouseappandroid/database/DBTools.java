package com.mouse.world.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mouse.world.app.App;

import org.json.JSONException;
import org.json.JSONObject;

public class DBTools extends SQLiteOpenHelper {

    //TODO: change back to internal!

    //internal:
    public static final String DATABASE_NAME = "MouseAppData.db";
//        external:
//        public static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mouseAppData.db";

    public static final int DATABASE_VERSION = 1;


    public DBTools(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ARTICLE_TABLE = "CREATE TABLE "
                + DBConstants.ARTICLE_TABLE_NAME + " ("
                + DBConstants.cityId + " TEXT, "
                + DBConstants.boneId + " TEXT, "
                + DBConstants.nsId + " TEXT, "
                + DBConstants.objId + " TEXT, "
                + DBConstants.menuItemId + " TEXT, "
                + DBConstants.rating + " REAL, "
                + DBConstants.ratingCount + " INTEGER, "
                + DBConstants.title + " TEXT, "
                + DBConstants.image + " TEXT, "
                + DBConstants.urlContent + " TEXT );";


        String CREATE_PLACE_TABLE = "CREATE TABLE "
                + DBConstants.PLACE_TABLE_NAME + " ("
                + DBConstants.id + " integer PRIMARY KEY autoincrement, "
                + DBConstants.cityId + " TEXT, "
                + DBConstants.boneId + " TEXT, "
                + DBConstants.nsId + " TEXT, "
                + DBConstants.objId + " TEXT, "
                + DBConstants.boneCategoryName + " TEXT, "
                + DBConstants.boneCategoryId + " INTEGER, "
                + DBConstants.address + " TEXT, "
                + DBConstants.description + " TEXT, "
                + DBConstants.fullDescriptionBody + " TEXT, "
                + DBConstants.hebrewName + " TEXT, "
                + DBConstants.name + " TEXT, "
                + DBConstants.rating + " REAL, "
                + DBConstants.ratingCount + " INTEGER, "
                + DBConstants.type + " TEXT, "
                + DBConstants.centerCoordinateLat + " TEXT, "
                + DBConstants.centerCoordinateLon + " TEXT, "
                + DBConstants.price + " INTEGER, "
                + DBConstants.image + " TEXT, "
                + DBConstants.phone + " TEXT, "
                + DBConstants.activityHours + " TEXT, "
                + DBConstants.publicTransportation + " TEXT, "
                + DBConstants.userComments + " TEXT );";


        String CREATE_CITY_TABLE = "CREATE TABLE "
                + DBConstants.CITY_TABLE_NAME + " ("
                + DBConstants.cityFolderPath + " TEXT, "
                + DBConstants.dateUpdated + " TEXT, "
                + DBConstants.cityId + " TEXT, "
                + DBConstants.hebrewName + " TEXT, "
                + DBConstants.name + " TEXT, "
                + DBConstants.centerCoordinateLat + " TEXT, "
                + DBConstants.centerCoordinateLon + " TEXT, "
                + DBConstants.stopsArticle + " TEXT, "
                + DBConstants.touristArticlesList + " TEXT );";


        String CREATE_FAVORITE_TABLE = "CREATE TABLE "
                + DBConstants.FAVORITE_TABLE_NAME + " ("
                + DBConstants.cityId + " TEXT, "
                + DBConstants.boneId + " TEXT, "
                + DBConstants.nsId + " TEXT, "
                + DBConstants.objId + " TEXT, "
                + DBConstants.boneCategoryName + " TEXT, "
                + DBConstants.type + " TEXT, "
                + DBConstants.name + " TEXT, "
                + DBConstants.hebrewName + " TEXT, "
                + DBConstants.description + " TEXT, "
                + DBConstants.address + " TEXT, "
                + DBConstants.phone + " TEXT, "
                + DBConstants.activityHours + " TEXT, "
                + DBConstants.publicTransportation + " TEXT, "
                + DBConstants.responses + " TEXT, "
                + DBConstants.imagePath + " TEXT );";

        try {
            db.execSQL(CREATE_CITY_TABLE);
            db.execSQL(CREATE_FAVORITE_TABLE);
            db.execSQL(CREATE_PLACE_TABLE);
            db.execSQL(CREATE_ARTICLE_TABLE);
            ContentValues values = new ContentValues();
            values.put(DBConstants.name, "נא להוריד מדריך");

            db.insert(DBConstants.PLACE_TABLE_NAME, null, values);

        } catch (SQLiteException e) {
            Log.e("Create table exception:", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.ARTICLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.CITY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.PLACE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.FAVORITE_TABLE_NAME);

        onCreate(db);
    }


    public void insertCityTable(JSONObject item) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(DBConstants.cityId, item.getString(DBConstants.cityId));
            values.put(DBConstants.hebrewName, item.getString(DBConstants.hebrewName));
            values.put(DBConstants.name, item.getString(DBConstants.name));
            values.put(DBConstants.centerCoordinateLat, item.getString(DBConstants.centerCoordinateLat));
            values.put(DBConstants.centerCoordinateLon, item.getString(DBConstants.centerCoordinateLon));
            values.put(DBConstants.stopsArticle, item.getString(DBConstants.stopsArticle));
            values.put(DBConstants.dateUpdated, item.getString(DBConstants.dateUpdated));
            values.put(DBConstants.cityFolderPath, item.getString(DBConstants.cityFolderPath));
            values.put(DBConstants.touristArticlesList, item.getString(DBConstants.touristArticlesList));

            database.insert(DBConstants.CITY_TABLE_NAME, null, values);

        } catch (JSONException e) {
//            e.printStackTrace();
            Log.wtf("catching: ", "insertCityTable" );
        }

        database.close();

    }

    public void insertArticleTable(JSONObject item) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(DBConstants.cityId, item.getString(DBConstants.cityId));
            values.put(DBConstants.nsId, item.getString(DBConstants.nsId));
            values.put(DBConstants.objId, item.getString(DBConstants.objId));
            values.put(DBConstants.boneId, item.getString(DBConstants.boneId));
            values.put(DBConstants.title, item.getString(DBConstants.title));
            values.put(DBConstants.menuItemId, item.getString(DBConstants.menuItemId));
            values.put(DBConstants.image, item.getString(DBConstants.image));
            values.put(DBConstants.rating, item.getString(DBConstants.rating));
            values.put(DBConstants.ratingCount, item.getString(DBConstants.ratingCount));

            values.put(DBConstants.urlContent, item.getString(DBConstants.urlContent));

            database.insert(DBConstants.ARTICLE_TABLE_NAME, null, values);

        } catch (JSONException e) {
//            e.printStackTrace();
            Log.wtf("catching: ", "insertArticleTable" );
        }

        database.close();

    }


    public void insertPlaceTable(JSONObject item) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(DBConstants.cityId, item.getString(DBConstants.cityId));
            values.put(DBConstants.name, item.getString(DBConstants.name));
            values.put(DBConstants.nsId, item.getString(DBConstants.nsId));
            values.put(DBConstants.objId, item.getString(DBConstants.objId));
            values.put(DBConstants.boneId, item.getString(DBConstants.boneId));
            values.put(DBConstants.description, item.getString(DBConstants.description));
            values.put(DBConstants.address, item.getString(DBConstants.address));
            values.put(DBConstants.phone, item.getString(DBConstants.phone));
            values.put(DBConstants.type, item.getString(DBConstants.type));
            values.put(DBConstants.rating, item.getString(DBConstants.rating));
            values.put(DBConstants.ratingCount, item.getString(DBConstants.ratingCount));

            JSONObject jsonUrlContentFullPlace = item.getJSONObject(DBConstants.urlContent);
            values.put(DBConstants.fullDescriptionBody, jsonUrlContentFullPlace.getString(DBConstants.description));
            values.put(DBConstants.hebrewName, jsonUrlContentFullPlace.getString(DBConstants.hebrewName));
            values.put(DBConstants.price, jsonUrlContentFullPlace.getString(DBConstants.price));
            values.put(DBConstants.image, jsonUrlContentFullPlace.getString(DBConstants.image));
            values.put(DBConstants.activityHours, jsonUrlContentFullPlace.getString(DBConstants.activityHours));
            values.put(DBConstants.publicTransportation, jsonUrlContentFullPlace.getString(DBConstants.publicTransportation));
            values.put(DBConstants.userComments, jsonUrlContentFullPlace.getString(DBConstants.userComments));

            database.insert(DBConstants.PLACE_TABLE_NAME, null, values);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        database.close();

    }


    public void insertPlaceFavorite(Cursor cursor) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            String sql = "SELECT 1 FROM " + DBConstants.FAVORITE_TABLE_NAME + " WHERE " + DBConstants.name + "=? AND " + DBConstants.cityId + "=?";
            Cursor checkCursor = database.rawQuery(sql, new String[]{cursor.getString(cursor.getColumnIndex(DBConstants.name)), cursor.getString(cursor.getColumnIndex(DBConstants.cityId))});

            if (checkCursor.getCount() == 0) {    //not already inside Favorites Table.
                values.put(DBConstants.cityId, cursor.getString(cursor.getColumnIndex(DBConstants.cityId)));
                values.put(DBConstants.boneId, cursor.getString(cursor.getColumnIndex(DBConstants.boneId)));
                values.put(DBConstants.nsId, cursor.getString(cursor.getColumnIndex(DBConstants.nsId)));
                values.put(DBConstants.objId, cursor.getString(cursor.getColumnIndex(DBConstants.objId)));
                values.put(DBConstants.boneCategoryName, cursor.getString(cursor.getColumnIndex(DBConstants.boneCategoryName)));
                values.put(DBConstants.type, cursor.getString(cursor.getColumnIndex(DBConstants.type)));
                values.put(DBConstants.name, cursor.getString(cursor.getColumnIndex(DBConstants.name)));
                values.put(DBConstants.hebrewName, cursor.getString(cursor.getColumnIndex(DBConstants.hebrewName)));

                database.insert(DBConstants.FAVORITE_TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        database.close();
    }


    public void insertArticleFavorite(JSONObject jsonObject) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String name = "", cityId = App.getInstance().get_cityId();
        try {
            name = jsonObject.getString(DBConstants.name).toString();

            String sql = "SELECT 1 FROM " + DBConstants.FAVORITE_TABLE_NAME + " WHERE " + DBConstants.name + "=? AND " + DBConstants.cityId + "=?";
            Cursor checkCursor = database.rawQuery(sql, new String[]{name, cityId});

            if (checkCursor.getCount() == 0) {    //not already inside Favorites Table.
                values.put(DBConstants.name, name);
                values.put(DBConstants.cityId, cityId);
                values.put(DBConstants.boneCategoryName, "כתבות");
                values.put(DBConstants.description, jsonObject.toString());

                database.insert(DBConstants.FAVORITE_TABLE_NAME, null, values);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        database.close();
    }


    public boolean isDataAlreadyInDB(String TableName, String colName, String rowValue) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TableName + " WHERE " + colName + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{rowValue});

        return cursor.getCount() > 0;
    }


    public void addDataToTable(String TableName, JSONObject item, String boneId) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {

            ContentValues newValues = new ContentValues();
            newValues.put(DBConstants.centerCoordinateLat, item.getString(DBConstants.centerCoordinateLat));
            newValues.put(DBConstants.centerCoordinateLon, item.getString(DBConstants.centerCoordinateLon));
            newValues.put(DBConstants.boneCategoryName, item.getString(DBConstants.boneCategoryName));
            newValues.put(DBConstants.boneCategoryId, item.getString(DBConstants.boneCategoryId));
            String condition = DBConstants.objId + "=? AND " + DBConstants.boneId + "=?";
            database.update(TableName, newValues, condition, new String[]{item.getString(DBConstants.objId), boneId});

        } catch (JSONException e) {
            e.printStackTrace();
        }
        database.close();

    }


    public String getCellData(String TableName, String ColumnReturned, String checkColumn, String checkVal) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT " + ColumnReturned + " FROM " + TableName + " WHERE " + checkColumn + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{checkVal});
        String res = null;
        if (cursor.moveToFirst()) {
            res = cursor.getString(cursor.getColumnIndex(ColumnReturned));
        }
        return res;
    }

    public String getCellData(String TableName, String ColumnReturned, String checkColumn1, String checkVal1, String checkColumn2, String checkVal2) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT " + ColumnReturned + " FROM " + TableName + " WHERE " + checkColumn1 + "=? AND " + checkColumn2 + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{checkVal1, checkVal2});
        String res = null;
        if (cursor.moveToFirst()) {
            res = cursor.getString(cursor.getColumnIndex(ColumnReturned));
        }
        return res;
    }


    public Cursor getData(String TableName, String checkColumn1, String checkVal1) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TableName + " WHERE " + checkColumn1 + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{checkVal1});
        cursor.moveToFirst();
        return cursor;
    }


    public Cursor getData(String TableName, String checkColumn1, String checkVal1, String checkColumn2, String checkVal2) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TableName + " WHERE " + checkColumn1 + "=? AND " + checkColumn2 + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{checkVal1, checkVal2});
        cursor.moveToFirst();
        return cursor;
    }


    public Cursor getData(String TableName, String checkColumn1, String checkVal1, String checkColumn2, String checkVal2, String checkColumn3, String checkVal3) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TableName + " WHERE " + checkColumn1 + "=? AND " + checkColumn2 + "=? AND " + checkColumn3 + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{checkVal1, checkVal2, checkVal3});
        cursor.moveToFirst();

        return cursor;
    }


    public Cursor getData(String TableName, String checkColumn1, String checkVal1, String checkColumn2, String checkVal2, String checkColumn3, String checkVal3, String checkColumn4, String checkVal4) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TableName + " WHERE " + checkColumn1 + "=? AND " + checkColumn2 + "=? AND " + checkColumn3 + "=? AND " + checkColumn4 + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{checkVal1, checkVal2, checkVal3, checkVal4});
        cursor.moveToFirst();

        return cursor;
    }


    public Cursor getFavorites(String TableName, String checkColumn1, String checkVal1, String checkColumn2, String checkVal2) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TableName + " WHERE " + checkColumn1 + "=? AND " + checkColumn2 + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{checkVal1, checkVal2});
        cursor.moveToFirst();
        return cursor;
    }


    public int deleteRow(String TableName, String KEY_NAME, String VALUE) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TableName, KEY_NAME + "=?", new String[]{VALUE});

    }

    public void deleteWholeCity(String cityId) {
        deleteRow(DBConstants.CITY_TABLE_NAME, DBConstants.cityId, cityId);
        deleteRow(DBConstants.PLACE_TABLE_NAME, DBConstants.cityId, cityId);
        deleteRow(DBConstants.ARTICLE_TABLE_NAME, DBConstants.cityId, cityId);
        deleteRow(DBConstants.FAVORITE_TABLE_NAME, DBConstants.cityId, cityId);
    }

    public void deleteWholeCityLeaveFavorites(String cityId) {
        deleteRow(DBConstants.CITY_TABLE_NAME, DBConstants.cityId, cityId);
        deleteRow(DBConstants.PLACE_TABLE_NAME, DBConstants.cityId, cityId);
        deleteRow(DBConstants.ARTICLE_TABLE_NAME, DBConstants.cityId, cityId);
    }


    public Cursor fetchItemsByDesc(String inputText) throws SQLException {

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor mCursor;

        if (App.getInstance().get_cityId() == null) {
            mCursor = database.query(true, DBConstants.PLACE_TABLE_NAME,
                    new String[]{DBConstants.name, DBConstants.hebrewName, DBConstants.id},
                    DBConstants.id + " = 1",
                    null, null, null, null, null);

        } else {


            mCursor = database.query(true, DBConstants.PLACE_TABLE_NAME,
                    new String[]{},
                    DBConstants.cityId + "=" + App.getInstance().get_cityId() + " AND (" +
                            DBConstants.name + " like '%" + inputText + "%'" + " OR " + DBConstants.hebrewName + " like '%" + inputText + "%')",
                    null, null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


}
