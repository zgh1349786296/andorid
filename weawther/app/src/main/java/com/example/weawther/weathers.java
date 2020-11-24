package com.example.weawther;

import java.util.List;

public class weathers {
    public String weathername;
    public List<weather> ws;
    public boolean concern;


    public boolean isConcern() {
        return concern;
    }

    public void setConcern(boolean concern) {
        this.concern = concern;
    }

    public String getWeathername() {
        return weathername;
    }

    public void setWeathername(String weathername) {
        this.weathername = weathername;
    }

    public List<weather> getWs() {
        return ws;
    }

    public void setWs(List<weather> ws) {
        this.ws = ws;
    }

    @Override
    public String toString() {
        return "weathers{" +
                "weathername='" + weathername + '\'' +
                ", ws=" + ws +
                '}';
    }
}
