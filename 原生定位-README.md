# 原生定位
 Android提供的原生定位方式

 ## Method and Attribute

构造方法
```
MapLocationClient()
```
常用方法
```
// 设置单次定位，默认否
setOnceLocation()
// 是否获取城市信息，默认仅获取定位坐标
setCityInfoRequired()
// 开始定位，当前方法会提示进行权限申请
// 如已经申请可填写注解 @SuppressLint("MissingPermission")
startLocation()
// 结束定位
stopLocation()
```

监听器
```
1. setLocationListener() 定位的监听
```


## About

注意：
1. 需要在清单文件进行申请权限 <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
2. 需要动态获取运行时权限 Manifest.permission.ACCESS_FINE_LOCATION 与 Manifest.permission.ACCESS_COARSE_LOCATION