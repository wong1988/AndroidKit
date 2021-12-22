package io.github.wong1988.kit.map;

import io.github.wong1988.kit.entity.MapLocation;

public interface MapLocationListener {

    // errorCode == -1 success
    // errorCode == 0 PASSIVE禁用
    // errorCode == 1 GPS禁用
    // errorCode == 2 NETWORK禁用
    void onLocationChanged(MapLocation mapLocation, int errorCode);
}
