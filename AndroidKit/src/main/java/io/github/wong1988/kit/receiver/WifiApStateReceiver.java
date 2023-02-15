package io.github.wong1988.kit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import io.github.wong1988.kit.AndroidKit;

/**
 * targetVersion 30
 * 荣耀 magic2 鸿蒙2 、华为 mate40e 鸿蒙3 、米10 Android12 测试通过
 */
public class WifiApStateReceiver extends BroadcastReceiver {

    private final WifiApStateListener listener;

    public WifiApStateReceiver(WifiApStateListener listener) {
        this.listener = listener;
    }

    /**
     * 注册广播
     */
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        AndroidKit.getInstance().getAppContext().registerReceiver(this, filter);
    }

    /**
     * 解除注册
     */
    public void unregisterReceiver() {
        AndroidKit.getInstance().getAppContext().unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())) {
                if (intent.getIntExtra("wifi_state", 0) == 12 || intent.getIntExtra("wifi_state", 0) == 13) {
                    if (listener != null)
                        listener.state(true);
                } else {
                    if (listener != null)
                        listener.state(false);
                }
            }
        } catch (Exception e) {
            Log.e("HotSpotStateReceiver", "onReceive()", e);
        }
    }

    public interface WifiApStateListener {
        void state(boolean isOpen);
    }
}
