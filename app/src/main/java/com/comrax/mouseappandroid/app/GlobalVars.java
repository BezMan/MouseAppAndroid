package com.mouse.world.app;

import android.content.Context;

import com.mouse.world.model.BannersModel;
import com.mouse.world.model.InitDataModel;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by betzalel on 16/04/2015.
 */
public class GlobalVars {

    public static String getBasePath(Context ctx, String fileName){
        return new File(ctx.getFilesDir(), fileName ).toString();
    }

    public static final String Default_masterFolder = "Default_master/";
    public static final String IconFolder = Default_masterFolder + "Images/MenuIcons/";

    public static final int[] boneColors = {0xFF73CDA4, 0xFF8F628C, 0xFFD7271A, 0xFF94C306};

    public static final String[] Drawer_textList = {"מדריך לערים נוספות", "המועדפים שלי", "שתף אפליקציה", "הוסף תגובה על האפליקציה", "עזרה", "תקנון", "אודות" , "", "", ""};
    public static final String[] Drawer_imageList = {"drawer0", "drawer1", "drawer2", "drawer3", "drawer4", "drawer5", "drawer6", "", "", "" };

    public static final String[] detailsListTitles = {"עצירות חובה", "מידע שימושי", "אפליקציית שיחות מחול", "טיסות לחול", "השכרת רכב", "ביטוח נסיעות"  };
    public static final String[] detailsListImages = {"must_see_BIG.png", "info_BIG.png", "228x216-bphone.jpg", "icons_flight_BIG.png", "spons_car_rent_BIG.png", "spons_insurance_BIG.png" };


    public static ArrayList<InitDataModel> initDataModelArrayList;

    public static ArrayList<String> detailMenuItems;

    public static ArrayList<BannersModel> BannersArray;

    public static JSONArray staticPagesArray;

    public static String[] icon = {"hotel", "shop", "rest", "tour"};

}

