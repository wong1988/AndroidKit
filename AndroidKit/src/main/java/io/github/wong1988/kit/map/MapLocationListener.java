package io.github.wong1988.kit.map;

import io.github.wong1988.kit.entity.MapLocation;

public interface MapLocationListener {

    // mapLocation == null error
    void location(MapLocation mapLocation, String error);
}
