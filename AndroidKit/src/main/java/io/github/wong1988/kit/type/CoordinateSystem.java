package io.github.wong1988.kit.type;

/**
 * 坐标系
 */
public enum CoordinateSystem {

    WGS84("地球坐标", "国际地图提供商，谷歌国际地图"),
    GCJ02("火星坐标，国测局坐标", "高德地图，谷歌地图，腾讯地图，阿里云地图"),
    BD09("百度坐标", "百度地图");

    private String alias;
    private String map;

    CoordinateSystem(String alias, String map) {
        this.alias = alias;
        this.map = map;
    }

    public String getAlias() {
        return alias;
    }

    public String getMap() {
        return map;
    }
}
