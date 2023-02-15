package io.github.wong1988.kit.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

import io.github.wong1988.kit.AndroidKit;
import io.github.wong1988.kit.task.WifiApAddress;

public class SettingsUtils {


    /**
     * 检查跳转个人热点页面是否存在
     */
    public static boolean isWifiApActivityExists(WifiApAddress apAddress) {

        if (apAddress == null)
            return false;

        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction("android.intent.action.MAIN");

        ComponentName cn = new ComponentName("com.android.settings", apAddress.getAddress());
        intent.setComponent(cn);

        PackageManager packageManager = AndroidKit.getInstance().getAppContext().getPackageManager();
        return packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null;
    }

    public static final int WIFI_AP_ACTIVITY_REQUEST_CODE = 6501;

    /**
     * // OPPO R15 Android10 荣耀 magic2 鸿蒙2 米10 Android12 Normal正确
     * // mate40e 鸿蒙3 Normal跳转不正确
     * 可参考demo进行优化
     */
    public static void startWifiApActivity(Activity activity, WifiApAddress apAddress, SettingsActivityNotFoundException e) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction("android.intent.action.MAIN");
        ComponentName cn = new ComponentName("com.android.settings", apAddress.getAddress());
        intent.setComponent(cn);

        PackageManager packageManager = AndroidKit.getInstance().getAppContext().getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null)
            activity.startActivityForResult(intent, WIFI_AP_ACTIVITY_REQUEST_CODE);
        else if (e != null)
            e.notFound();
    }

    public interface SettingsActivityNotFoundException {
        void notFound();
    }
}
