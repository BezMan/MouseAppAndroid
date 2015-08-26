package com.mouse.world.model;

/**
 * Created by betzalel on 15/04/2015.
 */
public class DrawerModel {

    private  String Text ="";
    private  String Image ="";


    public void setBtnTitle(String Text) { this.Text = Text; }

    public void setBtnImage(String Image)
    {
        this.Image = Image;
    }



    public String getBtnTitle()
    {
        return this.Text;
    }

    public String getBtnImage()
    {
        return this.Image;
    }

}
