package com.example.feather.model;

public class ForcastClass {
    String day;
    String icon;
    String temp;
    String min;
    String max;
    String wind;

    public ForcastClass() {
    }

    public ForcastClass(String day, String icon, String temp, String min, String max, String wind) {
        this.day = day;
        this.icon = icon;
        this.temp = temp;
        this.min = min;
        this.max = max;
        this.wind = wind;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
