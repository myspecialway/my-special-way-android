package org.myspecialway.ui.utils;

import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by dr9081 on 9/18/2018.
 */

public class AgendaListCreator {

    private LinkedHashMap<Integer, AgendaDataFrame> agendaDataFrameList = new LinkedHashMap<>();


//todo
    public String getServerContentFromHTTP() {

        return null;
    }

//todo
    public JSONObject getAgendaFileFromServer() {

        JSONObject jsonObject = null;

        return jsonObject;

    }

    public LinkedHashMap<Integer, AgendaDataFrame> generateAgendaList(){

        //todo generate from json, currently hardcoded just for test
        this.agendaDataFrameList.put(0,new AgendaDataFrame("קבלת תלמידים","8:00 - 7:00",""));
        this.agendaDataFrameList.put(1,new AgendaDataFrame("ארוחת בוקר","11:00 - 10:00",""));
        this.agendaDataFrameList.put(2,new AgendaDataFrame("הפסקה","12:00 - 11:00",""));
        this.agendaDataFrameList.put(3,new AgendaDataFrame("ארוחת צהריים","16:00 - 15:00",""));
        this.agendaDataFrameList.put(4,new AgendaDataFrame("הפסקה","17:00 - 16:00",""));
        this.agendaDataFrameList.put(5,new AgendaDataFrame("פיזור תלמידים","22:00 - 21:00",""));


        return this.agendaDataFrameList;
    }


    public int getNumberOfAgendaItems(){

        return agendaDataFrameList.size();
    }

}
