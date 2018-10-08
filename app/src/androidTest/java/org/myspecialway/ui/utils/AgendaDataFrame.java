package org.myspecialway.ui.utils;

/**
 * Created by dr9081 on 9/18/2018.
 */

public class AgendaDataFrame {

    private String title;
    private String timeFrame;
    private String image;


    public AgendaDataFrame(String title, String timeFrame,String image){
        this.image=image;
        this.timeFrame=timeFrame;
        this.title=title;
    }


    public String getTitle() {
        return title;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public String getImage() {
        return image;
    }
}
