package io.github.wong1988.kit.entity;

import io.github.wong1988.kit.utils.CoordTransformUtils;

/**
 * 地图定位实体类
 */
public class MapLocation {

    // 经度
    private final double longitude;
    // 纬度
    private final double latitude;
    // 是否包含以下城市信息
    private boolean hasCityInfo;
    // 省
    private String province;
    // 市
    private String city;
    // 县(区)
    private String country;
    // 街道
    private String street;
    // 街道号
    private String streetNum;
    // 地址
    private String address;

    public MapLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isHasCityInfo() {
        return hasCityInfo;
    }

    public void setHasCityInfo(boolean hasCityInfo) {
        this.hasCityInfo = hasCityInfo;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country == null ? "" : country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street == null ? "" : street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum == null ? "" : streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取原生定位坐标
     */
    public LatLng getWGS84LatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }

    /**
     * 获取高德地图坐标
     */
    public LatLng getGCJ024LatLng() {
        return CoordTransformUtils.WGS84ToGCJ02(getLongitude(), getLatitude());
    }

    /**
     * 获取百度定位坐标
     */
    public LatLng getBD09LatLng() {
        LatLng gcj02 = CoordTransformUtils.WGS84ToGCJ02(getLongitude(), getLatitude());
        return CoordTransformUtils.GCJ02ToBD09(gcj02.longitude, gcj02.latitude);
    }


    @Override
    public String toString() {
        if (hasCityInfo)
            return "MapLocation{" +
                    "longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", hasCityInfo=true" +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                    ", street='" + street + '\'' +
                    ", streetNum='" + streetNum + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        else
            return "MapLocation{" +
                    "longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", hasCityInfo=false" +
                    '}';
    }
}
