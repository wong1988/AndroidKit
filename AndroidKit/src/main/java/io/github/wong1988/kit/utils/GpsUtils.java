package io.github.wong1988.kit.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.annotation.NonNull;

public class GpsUtils {

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     */
    public static boolean isOpenGps(@NonNull Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 经过GPS卫星定位，定位级别能够精确到街（经过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 经过WLAN或移动网络(3G/2G)肯定的位置（也称做AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 开启GPS页面的请求码，可以在onActivityResult获取开启情况
     */
    public static final int GSP_ACTIVITY_REQUEST_CODE = 1588;

    /**
     * 打开开启GPS页面
     */
    public static void openGpsActivity(Activity activity, ActivityNotFoundException exception) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            activity.startActivityForResult(intent, GSP_ACTIVITY_REQUEST_CODE);
        } else {
            if (exception != null)
                exception.gpsActivityNotFound();
        }

    }

    /**
     * 打开开启GPS页面
     */
    public static void openGpsActivity(Fragment fragment, ActivityNotFoundException exception) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PackageManager packageManager = fragment.getActivity().getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            fragment.startActivityForResult(intent, GSP_ACTIVITY_REQUEST_CODE);
        } else {
            if (exception != null)
                exception.gpsActivityNotFound();
        }
    }

    public interface ActivityNotFoundException {
        void gpsActivityNotFound();
    }
}
