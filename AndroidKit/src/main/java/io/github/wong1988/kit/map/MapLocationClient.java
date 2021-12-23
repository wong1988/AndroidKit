package io.github.wong1988.kit.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.kit.AndroidKit;
import io.github.wong1988.kit.entity.MapLocation;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapLocationClient {

    private LocationManager mLocationManager;
    private boolean mOnceLocation;
    private boolean mCityInfoRequired = true;

    public MapLocationClient() {
        initLocationManager();
    }

    /**
     * 初始化
     */
    private void initLocationManager() {
        mLocationManager = (LocationManager) AndroidKit.getInstance().getAppContext().getSystemService(Context.LOCATION_SERVICE);
        getProviders();
    }

    private LocationListener mLocationListener;
    private String mBestProvider;

    private void getProviders() {
        Criteria criteria = new Criteria();
        // 查询精度：高，Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精确
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(true);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 是否允许产生资费 : 是
        criteria.setCostAllowed(true);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 获取最佳定位
        mBestProvider = mLocationManager.getBestProvider(criteria, false);

        mLocationListener = new LocationListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {

                if (mOnceLocation) {
                    stopLocation();
                }

                if (mMapLocationListener != null) {

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    MapLocation mapLocation = new MapLocation(longitude, latitude);

                    if (mCityInfoRequired) {
                        List<Address> addresses = new ArrayList<>();
                        // 经纬度转城市
                        Geocoder geocoder = new Geocoder(AndroidKit.getInstance().getAppContext());
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addresses != null && addresses.size() > 0) {

                            Address address = addresses.get(0);
                            String province = address.getAdminArea();
                            String city = address.getLocality();
                            String country = address.getSubLocality();
                            String street = address.getThoroughfare();
                            String streetNum = address.getSubThoroughfare();
                            String featureName = address.getFeatureName();

                            mapLocation.setAddress(featureName);
                            mapLocation.setProvince(province);
                            mapLocation.setCity(city);
                            mapLocation.setCountry(country);
                            mapLocation.setStreet(street);
                            mapLocation.setStreetNum(streetNum);
                            mapLocation.setHasCityInfo(true);
                        }
                    }

                    mMapLocationListener.onLocationChanged(mapLocation, -1);

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                switch (provider) {
                    case LocationManager.GPS_PROVIDER:
                        if (mMapLocationListener != null)
                            mMapLocationListener.onLocationChanged(null, 1);
                        break;
                    case LocationManager.NETWORK_PROVIDER:
                        if (mMapLocationListener != null)
                            mMapLocationListener.onLocationChanged(null, 2);
                        break;
                    case LocationManager.PASSIVE_PROVIDER:
                        if (mMapLocationListener != null)
                            mMapLocationListener.onLocationChanged(null, 0);
                        break;
                }
            }
        };

    }

    private MapLocationListener mMapLocationListener;

    /**
     * 设置定位监听
     */
    public void setLocationListener(MapLocationListener listener) {
        this.mMapLocationListener = listener;
    }

    /**
     * 开始定位
     */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void startLocation() {
        mLocationManager.requestLocationUpdates(mBestProvider, 0, 0, mLocationListener);
    }

    /**
     * 结束定位
     */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void stopLocation() {
        mLocationManager.removeUpdates(mLocationListener);
    }

    /**
     * 单次定位，默认否
     */
    public void setOnceLocation(boolean b) {
        mOnceLocation = b;
    }

    /**
     * 是否获取城市信息，默认获取
     */
    public void setCityInfoRequired(boolean b) {
        mCityInfoRequired = b;
    }
}
