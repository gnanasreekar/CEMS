package com.rgs.cems.Justclasses;

public class Model {
    String name,date,block,phase,report,urg,key;

    public Model(String name, String date, String block, String phase, String report , String Urg , String Key) {
        this.name = name;
        this.date = date;
        this.block = block;
        this.phase = phase;
        this.report = report;
        this.urg = Urg;
        this.key = Key;
    }

    public String getKey() {
        return key;
    }



    public String getUrg() {
        return urg;
    }



    public String getName() {
        return name;
    }


    public String getDate() {
        return date;
    }


    public String getBlock() {
        return block;
    }


    public String getPhase() {
        return phase;
    }


    public String getReport() {
        return report;
    }

}
