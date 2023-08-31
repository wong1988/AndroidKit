package io.github.wong1988.kit.utils;

import java.lang.reflect.Method;

public class HuaweiUtils {

    public static final String HARMONY = "harmony";

    /**
     * 获取华为系统
     */
    public static String getDeviceSystem() {

        String system;

        try {
            Class clz = Class.forName("com.huawei.system.BuildEx");
            Method method = clz.getMethod("getOsBrand");
            system = (String) method.invoke(clz);
            if (system == null)
                system = "";
        } catch (Exception e) {
            e.printStackTrace();
            system = "";
        }

        return system;
    }

    /**
     * 是否是鸿蒙系统
     */
    public static boolean isHarmony() {
        return HARMONY.equals(getDeviceSystem());
    }

    /**
     * 获取华为系统
     */
    public static String getDeviceSystemVersion() {

        String version;

        try {
            Class spClz = Class.forName("android.os.SystemProperties");
            Method method = spClz.getDeclaredMethod("get", String.class);
            version = (String) method.invoke(spClz, "hw_sc.build.platform.version");
            if (version == null)
                version = "";
        } catch (Throwable e) {
            e.printStackTrace();
            version = "";
        }

        return version;
    }
}
