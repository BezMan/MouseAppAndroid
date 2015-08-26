package com.mouse.world.model;

/**
 * Created by bez on 19/05/2015.
 */
public class InitDataModel {

    String CityId = "", File = "", Update_date = "";


    public void setCityId(String CityId)
    {
        this.CityId = CityId;
    }
    public void setFile(String File)
    {
        this.File = File;
    }
    public void setUpdate_date(String Update_date)
    {
        this.Update_date = Update_date;
    }



    public String getCityId()
    {
        return this.CityId;
    }
    public String getFile()
    {
        return this.File;
    }
    public String getUpdate_date()
    {
        return this.Update_date;
    }

}
