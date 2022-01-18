package io.github.wong1988.kit;

import android.app.Application;

/**
 * 初始化工具包
 */
public class AndroidKit {

    private static AndroidKit instance;

    private static Application mAppContext;

    private AndroidKit() {
        if (mAppContext == null)
            throw new RuntimeException("Please invoke AndroidKit.init(Application) on Application#onCreate()");
    }

    public static AndroidKit getInstance() {
        if (instance == null)
            synchronized (AndroidKit.class) {
                if (instance == null)
                    instance = new AndroidKit();
            }
        return instance;
    }

    // 需要在Application进行全局初始化
    public static void init(Application appContext) {
        mAppContext = appContext;
    }

    public Application getAppContext() {
        return mAppContext;
    }
}
