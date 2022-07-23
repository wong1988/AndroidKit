package io.github.wong1988.kit.utils;

import static android.content.Context.ACTIVITY_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresPermission;

import io.github.wong1988.kit.AndroidKit;

public class AppUtils {

    /**
     * 结束当前App后台进程
     */
    @RequiresPermission(Manifest.permission.KILL_BACKGROUND_PROCESSES)
    static void killBackgroundProgress() {
        ActivityManager activityMgr = (ActivityManager) AndroidKit.getInstance().getAppContext().getSystemService(ACTIVITY_SERVICE);
        activityMgr.killBackgroundProcesses(AndroidKit.getInstance().getAppContext().getPackageName());
    }

    /**
     * 结束当前App进程
     */
    static void killProgress() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 重启App(当前进程、后台进程都关闭),请自行实现关闭所有Activity的方法
     */
    @RequiresPermission(Manifest.permission.KILL_BACKGROUND_PROCESSES)
    public static void rebootAppSuper(Context context, ActivityCollector activityCollector) {
        if (activityCollector != null)
            activityCollector.finishAllActivity();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
            killBackgroundProgress();
            killProgress();
        }
    }

    /**
     * 重启App,请自行实现关闭所有Activity的方法
     */
    public static void rebootApp(Context context, ActivityCollector activityCollector) {
        if (activityCollector != null)
            activityCollector.finishAllActivity();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
            killProgress();
        }
    }

    /**
     * 重启App,请自行实现关闭所有Activity的方法
     */
    public static void rebootAppByAlarm(Context context, ActivityCollector activityCollector) {
        if (activityCollector != null)
            activityCollector.finishAllActivity();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent);
        killProgress();
    }

    public interface ActivityCollector {
        void finishAllActivity();
    }
}
