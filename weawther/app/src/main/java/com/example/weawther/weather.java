package com.example.weawther;

public class weather {
    public String cityname;
    public String summary;
    public String low;
    public String high;
    public String windDir;
    public String windPower;
    public String windState;
    public String humidity;
    public String time;
    public String temNow;

    @Override
    public String toString() {
        return "weather{" +
                "cityname='" + cityname + '\'' +
                ", summary='" + summary + '\'' +
                ", low='" + low + '\'' +
                ", high='" + high + '\'' +
                ", windDir='" + windDir + '\'' +
                ", windPower='" + windPower + '\'' +
                ", windState='" + windState + '\'' +
                ", humidity='" + humidity + '\'' +
                ", time='" + time + '\'' +
                ", temNow='" + temNow + '\'' +
                '}';
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getWindState() {
        return windState;
    }

    public void setWindState(String windState) {
        this.windState = windState;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemNow() {
        return temNow;
    }

    public void setTemNow(String temNow) {
        this.temNow = temNow;
    }
}
