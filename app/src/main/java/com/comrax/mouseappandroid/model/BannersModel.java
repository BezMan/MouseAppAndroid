package com.mouse.world.model;

/**
 * Created by betzalel on 30/03/2015.
 */
public class BannersModel {
    private  String text ="";
    private  String imageBIG ="";
    private  String urlAndroid ="";


    /*********** Set Methods ******************/
    public void setText(String text)
    {
        this.text = text;
    }

    public void setImageBIG(String imageBIG)
    {
        this.imageBIG = imageBIG;
    }

    public void setUrlAndroid(String urlAndroid) { this.urlAndroid = urlAndroid; }







    /*********** Get Methods ****************/
    public String getText()
    {
        return this.text;
    }

    public String getImageBIG()
    {
        return this.imageBIG;
    }

    public String getUrlAndroid()
    {
        return this.urlAndroid;
    }







}

