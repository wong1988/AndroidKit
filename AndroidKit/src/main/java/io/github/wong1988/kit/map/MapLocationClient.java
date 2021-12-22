package io.github.wong1988.kit.map;

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

    public MapLocationClient() {
        initLocationManager();
    }

    /**
     * 获取权限，并检查有无开户GPS
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
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        mBestProvider = mLocationManager.getBestProvider(criteria, false);  // 获取最佳定位
        mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                if (mMapLocationListener != null) {

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    MapLocation mapLocation = new MapLocation(longitude, latitude);

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
                    }

                    mMapLocationListener.location(mapLocation, "");

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
                if (!mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    if (mMapLocationListener != null)
                        mMapLocationListener.location(null, "GPS未开启");
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

}
