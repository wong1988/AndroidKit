package io.github.wong1988.kit.task;

public enum WifiApAddress {

    NORMAL("大部分手机可用，高版本荣耀、鸿蒙会跳转到《更多共享设置》", "com.android.settings.Settings$TetherSettingsActivity"),
    SPECIAL("高版本荣耀、鸿蒙使用《移动网络设置》进行热点设置", "com.android.settings.Settings$WirelessSettingsActivity");

    private String address;

    WifiApAddress(String describe, String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
