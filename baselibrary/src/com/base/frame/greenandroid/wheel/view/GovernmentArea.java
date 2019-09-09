package com.base.frame.greenandroid.wheel.view;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchengran on 2017/6/8.
 *
 * 三级行政区域 省  市  区县
 */


public class GovernmentArea {
    @SerializedName("name")
    private String provinceName;
    private int code;
    @SerializedName("city")
    private List<CityArea> listCity;

    //市
    public static class CityArea {
        @SerializedName("name")
        private String cityName;
        private int code;
        @SerializedName("area")
        private List<String> listCounty;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<String> getListCounty() {
            return listCounty;
        }

        public void setListCounty(List<String> listCounty) {
            this.listCounty = listCounty;
        }
    }

    //区县
    public static class countyArea {
        private String cityName;
        private int code;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CityArea> getListCity() {
        return listCity;
    }

    public List<String> getListCityName() {
        List<String> listName = new ArrayList<>();
        for (CityArea city : listCity) {
            listName.add(city.getCityName());
        }

        return listName;
    }

    public void setListCity(List<CityArea> listCity) {
        this.listCity = listCity;
    }
}
