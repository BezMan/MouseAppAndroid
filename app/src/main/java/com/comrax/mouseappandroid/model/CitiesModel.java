package com.mouse.world.model;

/**
 * Created by betzalel on 30/03/2015.
 */
public class CitiesModel {
    private  String Id ="";
    private  String boneId ="";
    private  String image ="";
    private  String name ="";


    /*********** Set Methods ******************/
    public void setId(String Id)
    {
        this.Id = Id;
    }

    public void setBoneId(String boneId)
    {
        this.boneId = boneId;
    }

    public void setImage(String image) { this.image = image; }

    public void setName(String name)
    {
        this.name = name;
    }






    /*********** Get Methods ****************/
    public String getId()
    {
        return this.Id;
    }

    public String getBoneId()
    {
        return this.boneId;
    }

    public String getImage()
    {
        return this.image;
    }

    public String getName()
    {
        return this.name;
    }






}

