package com.rgs.cems.Justclasses;

public class Model {
    String name,date,block,phase,report,urg;

    public Model(String name, String date, String block, String phase, String report , String Urg) {
        this.name = name;
        this.date = date;
        this.block = block;
        this.phase = phase;
        this.report = report;
        this.urg = Urg;
    }

    public String getUrg() {
        return urg;
    }

    public void setUrg(String urg) {
        this.urg = urg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
