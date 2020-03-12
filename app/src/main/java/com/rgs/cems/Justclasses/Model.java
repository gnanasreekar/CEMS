package com.rgs.cems.Justclasses;

public class Model {
    String name, date, block, phase, report, urg, key, status, reopned;

    public Model(String name, String date, String block, String phase, String report, String Urg, String Key, String status, String Reopened) {
        this.name = name;
        this.date = date;
        this.block = block;
        this.phase = phase;
        this.report = report;
        this.urg = Urg;
        this.key = Key;
        this.status = status;
        this.reopned = Reopened;
    }

    public String getReopned() {
        return reopned;
    }

    public String getStatus() {
        return status;
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
